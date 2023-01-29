package ns.io.clients.http

import kotlinx.serialization.Serializable

@Serializable
class HttpResponseWrapper<T> {
    var statusCode: Int? = null
    var headers: Map<String, List<String>>? = null
    var response: T? = null
}
