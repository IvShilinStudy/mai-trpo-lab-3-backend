package ru.ivn_sln.plugins

import io.ktor.server.application.*
import io.ktor.server.routing.*
import ru.ivn_sln.routes.OperationRoute

fun Application.configureRouting() {
    routing {
        OperationRoute()
    }
}
