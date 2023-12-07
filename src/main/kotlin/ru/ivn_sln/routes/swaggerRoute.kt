package ru.ivn_sln.routes

import io.ktor.server.plugins.swagger.*
import io.ktor.server.routing.*

fun Route.swaggerRoute(){
    swaggerUI(
        path = "/swagger",
        swaggerFile = "openapi/documentation.yaml"
    ) {
            version = "4.15.5"
    }
}