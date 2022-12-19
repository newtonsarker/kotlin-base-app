package ns.io.routes

import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.Routing
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import ns.io.routes.models.AddOrUpdateMerchantRequest

fun Routing.addOrUpdateMerchant() {
    post("/merchants") {
        val request = call.receive<AddOrUpdateMerchantRequest>()

        call.respond(request)
    }
}

fun Routing.getMerchantById() {
    get("/merchants/{id}") {

        //val loginRequest = call.receive<LoginRequest>()

        call.respond(mapOf("hello" to "world"))
    }
}