package ns.io.clients.http

interface HttpClient {

    fun post(url: String, jsonPayload: String): HttpResponseWrapper<String>
    fun get(url: String): HttpResponseWrapper<String>
    fun delete(url: String): HttpResponseWrapper<String>

}