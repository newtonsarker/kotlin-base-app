package ns.io.client.imagine.merchant

import kotlin.test.assertTrue
import ns.io.AppContext
import ns.io.InstanceFactory
import ns.io.client.ResponseStatus
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.SocketPolicy
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.BeforeClass
import org.junit.Test

class ImagineMerchantClientTest {

    private val mockWebServer = MockWebServer()
    private val client = InstanceFactory.get(ImagineMerchantClient::class.java)

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
    fun `should return success status if the client returns correct update response with status 200`() {
        // given
        mockWebServer.enqueue(MockResponse().setResponseCode(200).setBody("{\"merchantId\": 1001}"))

        // when
        val response = client.updateMerchant(createMerchant())

        // then
        assertEquals(ResponseStatus.SUCCESS, response.status)
        assertEquals(ResponseStatus.SUCCESS.name, response.statusMessage)
        assertEquals(1001, response.response!!.id)
    }

    @Test
    fun `should return failure status if the client returns status 400`() {
        // given
        mockWebServer.enqueue(MockResponse().setResponseCode(400))

        // when
        val response = client.updateMerchant(createMerchant())

        // then
        assertEquals(ResponseStatus.FAILURE, response.status)
        assertTrue(response.statusMessage!!.startsWith("Failed to create or update merchant."))
        assertNull(response.response)
    }

    @Test
    fun `should return error if the client fails to connect`() {
        // given
        mockWebServer.enqueue(MockResponse().setSocketPolicy(SocketPolicy.NO_RESPONSE))

        // when
        val response = client.updateMerchant(createMerchant())

        // then
        assertEquals(ResponseStatus.ERROR, response.status)
        assertTrue(response.statusMessage!!.startsWith("Failed to connect the client."))
        assertNull(response.response)
    }

    @Test
    fun `should return error if invalid response returned from the client`() {
        // given
        mockWebServer.enqueue(MockResponse().setResponseCode(200).setBody("sadffghjewrt"))

        // when
        val response = client.updateMerchant(createMerchant())

        // then
        assertEquals(ResponseStatus.ERROR, response.status)
        assertTrue(response.statusMessage!!.startsWith("Failed to parse response."))
        assertNull(response.response)
    }

    private fun createMerchant(): Merchant {
        val merchant = Merchant()
        merchant.name = "any_name"
        merchant.address = "any_address"
        return merchant
    }

}
