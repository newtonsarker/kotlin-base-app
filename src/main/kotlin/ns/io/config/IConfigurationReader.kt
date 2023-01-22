package ns.io.config

interface IConfigurationReader {

    fun readImagineMerchantBaseUrl(): String
    fun readImagineMerchantCreateApi(): String
    fun readImagineMerchantUpdateApi(): String
    fun readImagineMerchantDeleteApi(): String
    fun readImagineMerchantSelectApi(merchantId: String): String

}