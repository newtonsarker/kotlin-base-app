package ns.io.routes.models

import kotlinx.serialization.Serializable

@Serializable
data class AddOrUpdateMerchantRequest (
    val id: Int? = null,
    val name: String? = null,
    val address: String? = null
)