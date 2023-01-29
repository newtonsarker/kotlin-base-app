package ns.io.clients.imagine.merchant

import kotlinx.serialization.Serializable

@Serializable
class Merchant {
    var id: Int = 0
    var code: String? = null
    var name: String? = null
    var address: String? = null
}
