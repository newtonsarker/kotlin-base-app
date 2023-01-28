package ns.io.app.routes

import io.ktor.server.application.Application
import io.ktor.server.routing.routing


fun Application.configureRoutes() {
    routing {
        addOrUpdateMerchant()
        getMerchantById()
    }
}
