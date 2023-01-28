package ns.io.services

import kotlinx.serialization.Serializable

@Serializable
class MerchantStatus {
    var merchantId: Int = 0
    var isNew: Boolean = false
}
