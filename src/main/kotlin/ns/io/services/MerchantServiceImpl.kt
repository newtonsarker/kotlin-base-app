package ns.io.services

import ns.io.InstanceFactory
import ns.io.client.ResponseStatus
import ns.io.client.imagine.merchant.ImagineMerchantClient
import ns.io.client.imagine.merchant.Merchant
import ns.io.exceptions.MerchantCheckException

class MerchantServiceImpl : MerchantService {

    private val merchantClient = InstanceFactory.get(ImagineMerchantClient::class.java)

    override fun checkMerchant(merchant: Merchant): MerchantStatus {
        val merchantStatus = MerchantStatus()

        val selectResponse = merchantClient.selectMerchant(merchant.code!!)
        if (ResponseStatus.ERROR == selectResponse.status) {
            throw MerchantCheckException(
                errorCode = "1001",
                errorMessage = "Failed to check if the merchant exists.",
                exceptionMessage = selectResponse.statusMessage.toString()
            )
        } else {
            merchantStatus.isNew = ResponseStatus.FAILURE == selectResponse.status
        }

        val updateResponse = merchantClient.updateMerchant(merchant)
        if (ResponseStatus.ERROR == updateResponse.status) {
            throw MerchantCheckException(
                errorCode = "1002",
                errorMessage = "Failed to create or update merchant due to a technical reason.",
                exceptionMessage = selectResponse.statusMessage.toString()
            )
        } else if (ResponseStatus.FAILURE == updateResponse.status) {
            throw MerchantCheckException(
                errorCode = "1003",
                errorMessage = "Failed to create or update merchant due to a business reason.",
                exceptionMessage = updateResponse.statusMessage.toString()
            )
        } else {
            merchantStatus.merchantId = updateResponse.response!!.id
        }

        return merchantStatus
    }

}
