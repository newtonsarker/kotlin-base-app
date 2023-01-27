package ns.io

import io.ktor.server.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.server.netty.NettyApplicationEngine
import ns.io.app.routes.configurePlugins
import ns.io.app.routes.configureRoutes

fun main() {
    createApplicationEngine(Netty, 8080).start(wait = true)
}

private fun createApplicationEngine(appEngineFactory: Netty, appPort: Int): NettyApplicationEngine {
    AppContext.load()
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
