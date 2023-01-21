package ns.io.client

import io.ktor.http.HttpStatusCode
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

class ImagineMerchantClient {

    private val mediaType: MediaType = "application/json; charset=utf-8".toMediaType();
    private val client = OkHttpClient()
    private val url = "http://localhost:8081/merchant"

    private fun post(url: String, jsonPayload: String): HttpResponseWrapper<String> {
        val request: Request = Request.Builder()
            .url(url)
            .post(jsonPayload.toRequestBody(mediaType))
            .build()

        val response = client.newCall(request).execute()

        val httpResponse = HttpResponseWrapper<String>()
        httpResponse.statusCode = response.code
        httpResponse.headers = response.headers.toMultimap()
        httpResponse.response = response.body?.string()
        return httpResponse
    }

    fun createMerchant(merchant: Merchant): ClientResponseWrapper<Merchant> {
        val httpResponse = post(url, Json.encodeToString(Merchant.serializer(), merchant))

        val clientResponse = ClientResponseWrapper<Merchant>()
        if(httpResponse.statusCode == HttpStatusCode.OK.value) {
            clientResponse.status = ResponseStatus.SUCCESS
            clientResponse.statusMessage = ResponseStatus.SUCCESS.name
            // merchant.id =
        } else {
            clientResponse.status = ResponseStatus.FAILURE
            clientResponse.statusMessage = "Merchant creation failed"
        }
        return clientResponse
    }

}
