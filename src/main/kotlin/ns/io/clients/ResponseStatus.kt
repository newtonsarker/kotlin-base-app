package ns.io.clients

import kotlinx.serialization.Serializable

@Serializable
enum class ResponseStatus {
    SUCCESS, FAILURE, ERROR
}
