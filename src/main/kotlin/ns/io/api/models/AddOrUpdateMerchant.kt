package ns.io.api.models

import kotlinx.serialization.Serializable

@Serializable
class AddOrUpdateMerchant (
    var code: String? = null,
    var name: String? = null,
    var address: String? = null
)