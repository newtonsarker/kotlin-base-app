package ns.io.config

import java.io.File
import java.io.FileInputStream
import java.util.Properties

class ConfigurationReaderImpl: ConfigurationReader {

    private val prop = Properties()

    init {
        val url = ClassLoader.getSystemResource("app-config.properties")
        prop.load(FileInputStream(File(url.toURI())))
    }

    override fun readImagineMerchantBaseUrl(): String {
        return prop.getProperty("imagine.merchant.base.url")
    }

    override fun readImagineMerchantCreateApi(): String {
        return readImagineMerchantBaseUrl().plus(prop.getProperty("imagine.merchant.create"))
    }

    override fun readImagineMerchantDeleteApi(): String {
        return readImagineMerchantBaseUrl().plus(prop.getProperty("imagine.merchant.delete"))
    }

    override fun readImagineMerchantSelectApi(merchantId: String): String {
        return String.format(
            "%s%s", readImagineMerchantBaseUrl(), prop.getProperty("imagine.merchant.select")
        ).replace("{userId}", merchantId)
    }

}