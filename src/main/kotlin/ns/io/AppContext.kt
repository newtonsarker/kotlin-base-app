package ns.io

import ns.io.clients.imagine.merchant.ImagineMerchantClient
import ns.io.clients.imagine.merchant.ImagineMerchantClientImpl
import ns.io.clients.http.HttpClientImpl
import ns.io.clients.http.HttpClient
import ns.io.config.ConfigurationReaderImpl
import ns.io.config.ConfigurationReader
import ns.io.services.MerchantService
import ns.io.services.MerchantServiceImpl

object AppContext {

    fun init() {
        InstanceFactory.create(ConfigurationReader::class.java, ConfigurationReaderImpl())
        InstanceFactory.create(HttpClient::class.java, HttpClientImpl())
        InstanceFactory.create(ImagineMerchantClient::class.java, ImagineMerchantClientImpl())
        InstanceFactory.create(MerchantService::class.java, MerchantServiceImpl())
    }

}
