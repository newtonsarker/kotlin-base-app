package ns.io

import io.ktor.server.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.netty.NettyApplicationEngine
import ns.io.routes.configurePlugins
import ns.io.routes.configureRoutes

fun main() {
    createApplicationEngine(Netty, 8080).start(wait = true)
}

private fun createApplicationEngine(appEngineFactory: Netty, appPort: Int): NettyApplicationEngine {
    return embeddedServer(
        factory = appEngineFactory,
        port = appPort,
        module = Application::module
    )
}

private fun Application.module() {
    configurePlugins()
    configureRoutes()
}
