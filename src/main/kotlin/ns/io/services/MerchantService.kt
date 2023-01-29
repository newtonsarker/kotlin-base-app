package ns.io.services

import ns.io.clients.imagine.merchant.Merchant

interface MerchantService {

    fun checkMerchant(merchant: Merchant): MerchantStatus

}