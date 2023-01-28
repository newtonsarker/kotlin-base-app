package ns.io.client.imagine.merchant

import ns.io.client.ClientResponseWrapper

interface ImagineMerchantClient {

    fun updateMerchant(merchant: Merchant): ClientResponseWrapper<Merchant>
    fun selectMerchant(merchantCode: String): ClientResponseWrapper<Merchant>
    fun deleteMerchant(merchantId: Int): ClientResponseWrapper<String?>

}
