package ns.io.client.imagine.merchant

import ns.io.client.ClientResponseWrapper

interface ImagineMerchantClient {

    fun createMerchant(merchant: Merchant): ClientResponseWrapper<Merchant>

}