package ru.ivn_sln.plugins

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.testRoute(){
    get("/") {
        call.respondText { "Test respond" }
    }
}