package ru.ivn_sln.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import ru.ivn_sln.domain.repository.OperationRepository
import ru.ivn_sln.tools.whenFailure
import ru.ivn_sln.tools.whenSuccess

fun Route.operationRoute() {
    val repository: OperationRepository by inject()

    get("api/v1/operation/info") {
        val token = call.request.headers["Token"]

        if (token == null) {
            call.respond(HttpStatusCode.Unauthorized, "Auth token is null. Add it to headers")
        } else {
            repository.fetchOperations(token)
                .whenFailure { t ->
                    call.respond(HttpStatusCode.BadRequest, t.message ?: "")
                }
                .whenSuccess { operations ->
                    call.respond(operations)
                }
        }
    }

    get("api/v1/operation/{operationId}/extended") {
        val operationId = call.parameters["operationId"]?.toIntOrNull() ?: return@get
        val token = call.request.headers["Token"]

        if (token == null) {
            call.respond(HttpStatusCode.Unauthorized, "Auth token is null. Add it to headers")
        } else {
            repository.fetchFullOperation(operationId)
                .whenFailure { t ->
                    call.respond(HttpStatusCode.BadRequest, t.message ?: "")
                }
                .whenSuccess { operationExtended ->
                    call.respond(operationExtended)
                }
        }
    }
}