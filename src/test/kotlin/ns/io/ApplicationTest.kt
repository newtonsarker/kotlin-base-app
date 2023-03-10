package ns.io

import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.server.testing.testApplication
import kotlinx.serialization.json.Json
import ns.io.api.models.AddOrUpdateMerchant
import org.junit.Assert.assertEquals
import org.junit.Test

class ApplicationTest {

    private val json: Json = Json
    private val merchantRequest = AddOrUpdateMerchant(id = 1, name = "any_name", address = "any_address")

    @Test
    fun testRoot() = testApplication {
        initTestContext()
        client.post("/merchants") {
            val request = json.encodeToString(AddOrUpdateMerchant.serializer(), merchantRequest)
            contentType(ContentType.Application.Json)
            setBody(request)
        }.apply {
            val response = bodyAsText()
            val merchantResponse = json.decodeFromString(AddOrUpdateMerchant.serializer(), response)
            assertEquals(HttpStatusCode.OK, status)
            assertEquals(merchantResponse.id, merchantResponse.id)
        }
    }

}