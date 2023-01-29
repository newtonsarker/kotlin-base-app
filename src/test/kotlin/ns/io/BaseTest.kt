package ns.io

import io.ktor.server.testing.ApplicationTestBuilder
import ns.io.api.routes.configurePlugins
import ns.io.api.routes.configureRoutes

fun ApplicationTestBuilder.initTestContext() {
    application {
        configurePlugins()
        configureRoutes()
    }
}