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
import okhttp3.Response

class HttpClientImpl: HttpClient {

    private val mediaType: MediaType = "application/json; charset=utf-8".toMediaType()
    private val client = createHttpClient()

    override fun post(url: String, jsonPayload: String): HttpResponseWrapper<String> {
        val request = Request.Builder()
            .url(url)
            .post(jsonPayload.toRequestBody(mediaType))
            .build()

        val response = client.newCall(request).execute()

        return wrapResponse(response)
    }

    override fun get(url: String): HttpResponseWrapper<String> {
        val request = Request.Builder().url(url).get().build()
        val response = client.newCall(request).execute()
        return wrapResponse(response)
    }

    override fun delete(url: String): HttpResponseWrapper<String> {
        val request = Request.Builder().url(url).delete().build()
        val response = client.newCall(request).execute()
        return wrapResponse(response)
    }

    private fun wrapResponse(response: Response): HttpResponseWrapper<String> {
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
            .connectionPool(ConnectionPool(maxIdleConnections = 1000, keepAliveDuration = 300, TimeUnit.SECONDS))

            // The dispatcher is responsible for managing the execution of asynchronous requests and it uses a thread
            // pool to execute the requests. The maximum number of requests that can be executed simultaneously is
            // controlled by the maxRequests property, which sets the maximum number of requests that can be
            // executed concurrently.
            // By default, it uses a dispatcher that runs 64 requests in parallel.
            .dispatcher(Dispatcher().apply { maxRequests = 100 })

            // It uses timeouts to control how long to wait for a connection to be established,
            // how long to wait for a response, and how long to wait to read data.
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)

            // If the server compresses the response body using gzip, OkHttpClient can automatically
            // decompress it. This can reduce the amount of data that needs to be sent over the network.
            .addInterceptor(GzipRequestInterceptor())

            // OkHttpClient can retry a request if it fails due to a non-idempotent
            // method (like a POST request), a ConnectException, or a ProtocolException.
            //.retryOnConnectionFailure(true).addInterceptor(RetryInterceptor(3))

            .build()
    }

}