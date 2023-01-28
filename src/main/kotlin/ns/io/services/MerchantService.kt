package ns.io.services

import ns.io.client.imagine.merchant.Merchant

interface MerchantService {

    fun checkMerchant(merchant: Merchant): MerchantStatus

}