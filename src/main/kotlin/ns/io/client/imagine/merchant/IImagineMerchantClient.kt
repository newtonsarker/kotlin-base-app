package ns.io.client.imagine.merchant

import ns.io.client.ClientResponseWrapper

interface IImagineMerchantClient {

    fun createMerchant(merchant: Merchant): ClientResponseWrapper<Merchant>

}