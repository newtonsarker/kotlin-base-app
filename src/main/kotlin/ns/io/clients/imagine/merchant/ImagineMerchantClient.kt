package ns.io.clients.imagine.merchant

import ns.io.clients.ClientResponseWrapper

interface ImagineMerchantClient {

    fun updateMerchant(merchant: Merchant): ClientResponseWrapper<Merchant>
    fun selectMerchant(merchantCode: String): ClientResponseWrapper<Merchant>
    fun deleteMerchant(merchantId: Int): ClientResponseWrapper<String?>

}
