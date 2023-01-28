package ns.io.client.http

interface HttpClient {

    fun post(url: String, jsonPayload: String): HttpResponseWrapper<String>
    fun get(url: String): HttpResponseWrapper<String>
    fun delete(url: String): HttpResponseWrapper<String>

}