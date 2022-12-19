package ns.io

import io.ktor.server.testing.ApplicationTestBuilder
import ns.io.app.routes.configurePlugins
import ns.io.app.routes.configureRoutes

fun ApplicationTestBuilder.initTestContext() {
    application {
        configurePlugins()
        configureRoutes()
    }
}