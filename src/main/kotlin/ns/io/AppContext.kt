package ns.io

import ns.io.client.imagine.merchant.IImagineMerchantClient
import ns.io.client.imagine.merchant.ImagineMerchantClient
import ns.io.client.http.HttpClient
import ns.io.client.http.IHttpClient
import ns.io.config.ConfigurationReader
import ns.io.config.IConfigurationReader

object AppContext {

    fun load() {
        InstanceFactory.create(IConfigurationReader::class.java, ConfigurationReader())
        InstanceFactory.create(IHttpClient::class.java, HttpClient())
        InstanceFactory.create(IImagineMerchantClient::class.java, ImagineMerchantClient())
    }

}
