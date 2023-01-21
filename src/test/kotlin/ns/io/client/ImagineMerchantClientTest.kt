package ns.io.client

import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ImagineMerchantClientTest {

    private val mockWebServer = MockWebServer()
    private val client = ImagineMerchantClient()

    @Before
    fun setUp() {
        mockWebServer.start(8081)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun testServerResponse() {
        // given
        mockWebServer.enqueue(MockResponse()
            .setResponseCode(200)
            .setBody("{\"merchantId\": 1001}")
        )

        // when
        val merchant = Merchant()
        merchant.name = "any_name"
        merchant.address = "any_address"
        val response = client.createMerchant(merchant)

        // then
        assertEquals(ResponseStatus.SUCCESS, response.status)
        assertEquals(ResponseStatus.SUCCESS.name, response.statusMessage)
    }

}
