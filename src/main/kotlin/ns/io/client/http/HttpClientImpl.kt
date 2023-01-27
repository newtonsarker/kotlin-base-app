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

class HttpClientImpl: HttpClient {

    private val mediaType: MediaType = "application/json; charset=utf-8".toMediaType()
    private val client = createHttpClient()

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

    private fun createHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()

            // Connection pool that keeps track of available connections to reuse them instead of creating a new
            // connection for each request. By default, it holds 5 connections per host.
            // maxIdleConnections: maximum number of idle connections to each route
            // keepAliveDuration: how long idle connections should be kept alive
            .connectionPool(ConnectionPool(maxIdleConnections = 50, keepAliveDuration = 30, TimeUnit.SECONDS))

            // Dispatcher: OkHttpClient uses a Dispatcher to manage the execution of requests. By default, it uses a
            // Dispatcher that runs 64 requests in parallel.
            .dispatcher(Dispatcher().apply { maxRequests = 100 })

            // Timeouts: OkHttpClient uses timeouts to control how long to wait for a connection to be established,
            // how long to wait for a response, and how long to wait to read data.
            // Timeouts can be increased to handle slow or unresponsive servers.
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)

            // Gzip: If the server compresses the response body using gzip, OkHttpClient can automatically
            // decompress it. This can reduce the amount of data that needs to be sent over the network.
            .addInterceptor(GzipRequestInterceptor())

            // Retry policy: OkHttpClient can retry a request if it fails due to a non-idempotent
            // method (like a POST request), a ConnectException, or a ProtocolException.
            //.retryOnConnectionFailure(true).addInterceptor(RetryInterceptor(3))

            .build()
    }

}