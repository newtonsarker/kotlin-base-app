package ns.io.api.models

import kotlinx.serialization.Serializable

@Serializable
class ApiRequest<T> {
    var request: T? = null
}