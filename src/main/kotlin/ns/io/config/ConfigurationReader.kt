package ns.io.config

interface ConfigurationReader {

    fun readImagineMerchantBaseUrl(): String
    fun readImagineMerchantCreateApi(): String
    fun readImagineMerchantDeleteApi(): String
    fun readImagineMerchantSelectApi(merchantId: String): String

}