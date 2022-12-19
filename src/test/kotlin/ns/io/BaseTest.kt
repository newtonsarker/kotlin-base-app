package ns.io

import io.ktor.server.testing.ApplicationTestBuilder
import ns.io.routes.configurePlugins
import ns.io.routes.configureRoutes

fun ApplicationTestBuilder.initTestContext() {
    application {
        configurePlugins()
        configureRoutes()
    }
}