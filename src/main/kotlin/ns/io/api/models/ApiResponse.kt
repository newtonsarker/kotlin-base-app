package ns.io.api.models

import kotlinx.serialization.Serializable

@Serializable
class ApiResponse<T> {
    var isOk: Boolean = false
    var errorCode: String? = null
    var errorMessage: String? = null
    var response: T? = null
}