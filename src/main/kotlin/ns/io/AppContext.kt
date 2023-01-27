package ns.io

import ns.io.client.imagine.merchant.ImagineMerchantClient
import ns.io.client.imagine.merchant.ImagineMerchantClientImpl
import ns.io.client.http.HttpClientImpl
import ns.io.client.http.HttpClient
import ns.io.config.ConfigurationReaderImpl
import ns.io.config.ConfigurationReader

object AppContext {

    fun load() {
        InstanceFactory.create(ConfigurationReader::class.java, ConfigurationReaderImpl())
        InstanceFactory.create(HttpClient::class.java, HttpClientImpl())
        InstanceFactory.create(ImagineMerchantClient::class.java, ImagineMerchantClientImpl())
    }

}
