package ns.io.client.http

interface IHttpClient {

    fun post(url: String, jsonPayload: String): HttpResponseWrapper<String>

}