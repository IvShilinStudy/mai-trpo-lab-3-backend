package ru.ivn_sln.plugins

import io.ktor.server.application.*
import io.ktor.server.routing.*
import ru.ivn_sln.routes.operationRoute
import ru.ivn_sln.routes.swaggerRoute

fun Application.configureRouting() {
    routing {
        testRoute()
        operationRoute()
    }
}