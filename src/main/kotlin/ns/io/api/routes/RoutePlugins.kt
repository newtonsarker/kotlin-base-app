package ns.io.api.routes

import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation


fun Application.configurePlugins() {

    install(ContentNegotiation) { json() }

}