package ns.io.api.routes

import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.post
import ns.io.InstanceFactory
import ns.io.api.models.AddOrUpdateMerchant
import ns.io.api.models.ApiRequest
import ns.io.api.models.ApiResponse
import ns.io.clients.imagine.merchant.Merchant
import ns.io.exceptions.MerchantCheckException
import ns.io.services.MerchantService
import ns.io.services.MerchantStatus

private val merchantService = InstanceFactory.get(MerchantService::class.java)

fun Routing.addOrUpdateMerchant() {
    post("/merchant") {
        val requestPayload = call.receive<ApiRequest<AddOrUpdateMerchant>>()

        val apiResponse = ApiResponse<MerchantStatus>()
        try {
            apiResponse.isOk = true
            apiResponse.response = merchantService.checkMerchant(createMerchantFromRequest(requestPayload.request!!))
        } catch (exception: Exception) {
            call.response.status(HttpStatusCode.InternalServerError)
            apiResponse.isOk = false
            when (exception) {
                is MerchantCheckException -> {
                    apiResponse.errorCode = exception.errorCode
                    apiResponse.errorMessage = exception.errorMessage
                }
                else -> {
                    apiResponse.errorCode = "7000"
                    apiResponse.errorMessage = "System failure"
                }
            }
        }
        call.respond(apiResponse)
    }
}

private fun createMerchantFromRequest(request: AddOrUpdateMerchant): Merchant {
    val merchant = Merchant()
    merchant.code = request.code
    merchant.name = request.name
    merchant.address = request.address
    return merchant
}
