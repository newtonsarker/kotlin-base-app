package ns.io.clients.imagine.merchant

import io.ktor.http.HttpStatusCode
import java.io.IOException
import kotlinx.serialization.SerializationException
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ns.io.InstanceFactory
import ns.io.clients.ClientResponseWrapper
import ns.io.clients.ResponseStatus
import ns.io.clients.http.HttpClient
import ns.io.config.ConfigurationReader

class ImagineMerchantClientImpl: ImagineMerchantClient {

    private val config = InstanceFactory.get(ConfigurationReader::class.java)
    private val httpClient = InstanceFactory.get(HttpClient::class.java)

    override fun updateMerchant(merchant: Merchant): ClientResponseWrapper<Merchant> {
        val clientResponse = ClientResponseWrapper<Merchant>()
        try {
            val httpResponse = httpClient.post(
                config.readImagineMerchantCreateApi(),
                Json.encodeToString(Merchant.serializer(), merchant)
            )

            if(httpResponse.statusCode == HttpStatusCode.OK.value && !httpResponse.response.isNullOrEmpty()) {
                val updateMerchantResponse = Json.decodeFromString(
                    UpdateMerchantResponse.serializer(), httpResponse.response!!
                )
                merchant.id = updateMerchantResponse.merchantId!!

                clientResponse.status = ResponseStatus.SUCCESS
                clientResponse.statusMessage = ResponseStatus.SUCCESS.name
                clientResponse.response = merchant
            } else {
                clientResponse.status = ResponseStatus.FAILURE
                clientResponse.statusMessage = "Failed to create or update merchant. ".plus(Json.encodeToString(httpResponse))
            }
        } catch (exception: Exception) {
            clientResponse.status = ResponseStatus.ERROR
            clientResponse.statusMessage = extractExceptionMessage(exception)
        }
        return clientResponse
    }

    override fun selectMerchant(merchantCode: String): ClientResponseWrapper<Merchant> {
        val clientResponse = ClientResponseWrapper<Merchant>()
        try {
            val httpResponse = httpClient.get(
                config.readImagineMerchantCreateApi().replace("{merchant_code}", merchantCode)
            )

            if(httpResponse.statusCode == HttpStatusCode.OK.value && !httpResponse.response.isNullOrEmpty()) {
                val merchant = Json.decodeFromString(
                    Merchant.serializer(), httpResponse.response!!
                )
                clientResponse.status = ResponseStatus.SUCCESS
                clientResponse.statusMessage = ResponseStatus.SUCCESS.name
                clientResponse.response = merchant
            } else {
                clientResponse.status = ResponseStatus.FAILURE
                clientResponse.statusMessage = "Merchant not found. ".plus(Json.encodeToString(httpResponse))
            }
        } catch (exception: Exception) {
            clientResponse.status = ResponseStatus.ERROR
            clientResponse.statusMessage = extractExceptionMessage(exception)
        }
        return clientResponse
    }

    override fun deleteMerchant(merchantId: Int): ClientResponseWrapper<String?> {
        val clientResponse = ClientResponseWrapper<String?>()
        try {
            val httpResponse = httpClient.delete(
                config.readImagineMerchantCreateApi().replace("{merchant_id}", merchantId.toString())
            )

            if(httpResponse.statusCode == HttpStatusCode.OK.value) {
                clientResponse.status = ResponseStatus.SUCCESS
                clientResponse.statusMessage = ResponseStatus.SUCCESS.name
                clientResponse.response = httpResponse.response
            } else {
                clientResponse.status = ResponseStatus.FAILURE
                clientResponse.statusMessage = "Failed to process invalid response. ".plus(Json.encodeToString(httpResponse))
            }
        } catch (exception: Exception) {
            clientResponse.status = ResponseStatus.ERROR
            clientResponse.statusMessage = extractExceptionMessage(exception)
        }
        return clientResponse
    }

    private fun extractExceptionMessage(exception: Exception): String {
        return when (exception) {
            is IOException -> "Failed to connect the client. ".plus(exception.stackTraceToString())
            is SerializationException -> "Failed to parse response. ".plus(exception.stackTraceToString())
            else -> exception.stackTraceToString()
        }
    }

}
