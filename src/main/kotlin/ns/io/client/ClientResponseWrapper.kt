package ns.io.client

import kotlinx.serialization.Serializable

@Serializable
class ClientResponseWrapper<T> {
    var status: ResponseStatus? = null
    var statusMessage: String? = null
    var response: T? = null
}