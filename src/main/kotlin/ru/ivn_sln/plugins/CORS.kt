package ru.ivn_sln.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.cors.routing.*

fun Application.installCors() {
    install(CORS) {
        allowHeader(HttpHeaders.ContentType)
        // TODO Выключить на проде
        anyHost()
    }
}