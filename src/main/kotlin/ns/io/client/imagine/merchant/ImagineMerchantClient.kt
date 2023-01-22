package ns.io.client.imagine.merchant

import io.ktor.http.HttpStatusCode
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import ns.io.InstanceFactory
import ns.io.client.ClientResponseWrapper
import ns.io.client.ResponseStatus
import ns.io.client.http.IHttpClient
import ns.io.config.IConfigurationReader

class ImagineMerchantClient: IImagineMerchantClient {

    private val config = InstanceFactory.get(IConfigurationReader::class.java)
    private val httpClient = InstanceFactory.get(IHttpClient::class.java)

    override fun createMerchant(merchant: Merchant): ClientResponseWrapper<Merchant> {
        val httpResponse = httpClient.post(
            config.readImagineMerchantCreateApi(),
            Json.encodeToString(Merchant.serializer(), merchant)
        )

        val clientResponse = ClientResponseWrapper<Merchant>()
        if(httpResponse.statusCode == HttpStatusCode.OK.value && !httpResponse.response.isNullOrEmpty()) {
            try {
                val createMerchantResponse = Json.decodeFromString(
                    CreateMerchantResponse.serializer(), httpResponse.response!!
                )
                merchant.id = createMerchantResponse.merchantId!!
                clientResponse.status = ResponseStatus.SUCCESS
                clientResponse.statusMessage = ResponseStatus.SUCCESS.name
                clientResponse.response = merchant
            } catch (exception: SerializationException) {
                clientResponse.status = ResponseStatus.FAILURE
                clientResponse.statusMessage = "Merchant creation failed"
            }
        } else {
            clientResponse.status = ResponseStatus.FAILURE
            clientResponse.statusMessage = "Merchant creation failed"
        }
        return clientResponse
    }

}

