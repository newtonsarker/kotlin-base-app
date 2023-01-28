package ns.io.services

import kotlin.test.assertTrue
import kotlinx.serialization.json.Json
import ns.io.AppContext
import ns.io.InstanceFactory
import ns.io.client.imagine.merchant.Merchant
import ns.io.exceptions.MerchantCheckException
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.SocketPolicy
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

class MerchantServiceTest {

    private val mockWebServer = MockWebServer()
    private val service = InstanceFactory.get(MerchantService::class.java)

    companion object {
        @BeforeClass
        @JvmStatic
        fun init() {
            AppContext.init()
        }
    }

    @Before
    fun setUp() {
        mockWebServer.start(8080)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun `should throw exception if http client fails to connect to search api`() {
        // given
        mockWebServer.enqueue(MockResponse().setSocketPolicy(SocketPolicy.NO_RESPONSE))
        val merchant = createMerchant(0)

        // when
        val exception = assertThrows(MerchantCheckException::class.java){
            service.checkMerchant(merchant)
        }

        // then
        assertEquals("1001", exception.errorCode)
        assertTrue(exception.errorMessage.startsWith("Failed to check if the merchant exists"))
        assertTrue(exception.message!!.startsWith("Failed to connect the client."))
    }

    @Test
    fun `should throw exception if http client fails to connect to update api`() {
        // given
        mockWebServer.enqueue(MockResponse().setResponseCode(404))
        mockWebServer.enqueue(MockResponse().setSocketPolicy(SocketPolicy.NO_RESPONSE))
        val merchant = createMerchant(0)

        // when
        val exception = assertThrows(MerchantCheckException::class.java){
            service.checkMerchant(merchant)
        }

        // then
        assertEquals("1002", exception.errorCode)
        assertTrue(exception.errorMessage.startsWith("Failed to create or update merchant due to a technical reason."))
        assertTrue(exception.message!!.startsWith("Merchant not found."))
    }

    @Test
    fun `should throw exception if update api fails to create or update merchant`() {
        // given
        mockWebServer.enqueue(MockResponse().setResponseCode(404))
        mockWebServer.enqueue(MockResponse().setResponseCode(400))
        val merchant = createMerchant(0)

        // when
        val exception = assertThrows(MerchantCheckException::class.java){
            service.checkMerchant(merchant)
        }

        // then
        assertEquals("1003", exception.errorCode)
        assertTrue(exception.errorMessage.startsWith("Failed to create or update merchant due to a business reason."))
        assertTrue(exception.message!!.startsWith("Failed to create or update merchant."))
    }

    @Test
    fun `should return existing merchant status if the merchant is found from the api`() {
        // given
        val merchant = createMerchant(1)
        mockWebServer.enqueue(MockResponse().setResponseCode(200).setBody(Json.encodeToString(Merchant.serializer(), merchant)))
        mockWebServer.enqueue(MockResponse().setResponseCode(200).setBody("{\"merchantId\": ${merchant.id} }"))

        // when
        val merchantStatus = service.checkMerchant(merchant)

        // then
        assertEquals(false, merchantStatus.isNew)
        assertEquals(1, merchantStatus.merchantId)
    }

    @Test
    fun `should return new merchant status if the merchant is not found from the api`() {
        // given
        val merchant = createMerchant(2)
        mockWebServer.enqueue(MockResponse().setResponseCode(404))
        mockWebServer.enqueue(MockResponse().setResponseCode(200).setBody("{\"merchantId\": ${merchant.id} }"))

        // when
        val merchantStatus = service.checkMerchant(merchant)

        // then
        assertEquals(true, merchantStatus.isNew)
        assertEquals(2, merchantStatus.merchantId)
    }

    private fun createMerchant(merchantId: Int): Merchant {
        val merchant = Merchant()
        merchant.id = merchantId
        merchant.code = "any_code"
        merchant.name = "any_name"
        merchant.address = "any_address"
        return merchant
    }

}
