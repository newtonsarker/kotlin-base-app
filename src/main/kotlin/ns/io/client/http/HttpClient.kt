package ns.io.client.http

import java.util.concurrent.TimeUnit
import ns.io.client.http.interceptors.GzipRequestInterceptor
import okhttp3.ConnectionPool
import okhttp3.Dispatcher
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

class HttpClient: IHttpClient {

    private val mediaType: MediaType = "application/json; charset=utf-8".toMediaType()
    private val client = OkHttpClient.Builder()
        .connectionPool(ConnectionPool(maxIdleConnections = 50, keepAliveDuration = 30, TimeUnit.SECONDS))
        .dispatcher(Dispatcher().apply { maxRequests = 100 })
        .connectTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .addInterceptor(GzipRequestInterceptor())
        //.retryOnConnectionFailure(true).addInterceptor(RetryInterceptor(3))
        .build()

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