package ns.io.client

import kotlinx.serialization.Serializable

@Serializable
class Merchant {
    var id: Int = 0
    var name: String? = null
    var address: String? = null
}
