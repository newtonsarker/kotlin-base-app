package ns.io.client.http

interface HttpClient {

    fun post(url: String, jsonPayload: String): HttpResponseWrapper<String>

}