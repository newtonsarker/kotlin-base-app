package ns.io.client

import kotlinx.serialization.Serializable

@Serializable
enum class ResponseStatus {
    SUCCESS, FAILURE, ERROR
}
