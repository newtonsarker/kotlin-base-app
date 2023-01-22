package ns.io.client.imagine.merchant

import io.ktor.http.HttpStatusCode
import java.io.IOException
import kotlinx.serialization.SerializationException
import kotlinx.serialization.encodeToString
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
        val clientResponse = ClientResponseWrapper<Merchant>()
        try {
            val httpResponse = httpClient.post(
                config.readImagineMerchantCreateApi(),
                Json.encodeToString(Merchant.serializer(), merchant)
            )

            if(httpResponse.statusCode == HttpStatusCode.OK.value && !httpResponse.response.isNullOrEmpty()) {
                val createMerchantResponse = Json.decodeFromString(
                    CreateMerchantResponse.serializer(), httpResponse.response!!
                )
                merchant.id = createMerchantResponse.merchantId!!

                clientResponse.status = ResponseStatus.SUCCESS
                clientResponse.statusMessage = ResponseStatus.SUCCESS.name
                clientResponse.response = merchant
            } else {
                clientResponse.status = ResponseStatus.FAILURE
                clientResponse.statusMessage = "Failed to process invalid response. ".plus(Json.encodeToString(httpResponse))
            }
        } catch (exception: Exception) {
            when (exception) {
                is IOException -> {
                    clientResponse.status = ResponseStatus.ERROR
                    clientResponse.statusMessage = "Failed to connect the client. ".plus(exception.stackTraceToString())
                }
                is SerializationException -> {
                    clientResponse.status = ResponseStatus.ERROR
                    clientResponse.statusMessage = "Failed to parse response. ".plus(exception.stackTraceToString())
                }
                else -> {
                    clientResponse.status = ResponseStatus.ERROR
                    clientResponse.statusMessage = exception.stackTraceToString()
                }
            }
        }
        return clientResponse
    }

}
