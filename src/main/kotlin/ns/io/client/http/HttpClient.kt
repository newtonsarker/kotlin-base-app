package ns.io.client.http

import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

class HttpClient: IHttpClient {

    private val mediaType: MediaType = "application/json; charset=utf-8".toMediaType();
    private val client = OkHttpClient()

    override fun post(url: String, jsonPayload: String): HttpResponseWrapper<String> {
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

}