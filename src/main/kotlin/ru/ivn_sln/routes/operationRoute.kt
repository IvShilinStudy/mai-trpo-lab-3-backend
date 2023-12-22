package ru.ivn_sln.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject
import ru.ivn_sln.data.request.OperationInsertRequest
import ru.ivn_sln.data.request.OperationUpdateRequest
import ru.ivn_sln.data.request.ReportCreateRequest
import ru.ivn_sln.data.response.RegUser
import ru.ivn_sln.domain.repository.OperationRepository
import ru.ivn_sln.tools.whenFailure
import ru.ivn_sln.tools.whenSuccess

fun Route.operationRoute() {
    val repository: OperationRepository by inject()

    get("api/v1/{token}/operation/info") {
        val token = call.parameters["token"]

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

    get("api/v1/{token}/operation/{operationId}/extended") {
        val operationId = call.parameters["operationId"]?.toIntOrNull() ?: return@get
        val token = call.parameters["token"]

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

    post("api/v1/{token}/operation/add") {
        val token = call.parameters["token"]
        val data = call.receive<OperationInsertRequest>()

        if (token == null) {
            call.respond(HttpStatusCode.Unauthorized, "Auth token is null. Add it to headers")
        } else {
            repository.addOperation(
                token,
                data
            )
                .whenFailure { t ->
                    call.respond(HttpStatusCode.BadRequest, t.message ?: "")
                }
                .whenSuccess {
                    call.respond("Успешно")
                }
        }
    }

    post("api/v1/{token}/operation/{operationId}/replace") {
        val token = call.parameters["token"]
        val operationId = call.parameters["operationId"]?.toIntOrNull() ?: return@post
        val data = call.receive<OperationUpdateRequest>()

        if (token == null) {
            call.respond(HttpStatusCode.Unauthorized, "Auth token is null. Add it to headers")
        } else {
            repository.changeOperation(
                operationId,
                data
            )
                .whenFailure { t ->
                    call.respond(HttpStatusCode.BadRequest, t.message ?: "")
                }
                .whenSuccess {
                    call.respond("Успешно")
                }
        }
    }

    delete("api/v1/{token}/operation/{operationId}/delete") {
        val token = call.parameters["token"]
        val operationId = call.parameters["operationId"]?.toIntOrNull() ?: return@delete

        if (token == null) {
            call.respond(HttpStatusCode.Unauthorized, "Auth token is null. Add it to headers")
        } else {
            repository.deleteOperation(operationId)
                .whenFailure { t ->
                    call.respond(HttpStatusCode.BadRequest, t.message ?: "")
                }
                .whenSuccess {
                    call.respond("Успешно")
                }
        }
    }

    post("api/v1/registration/{token}") {
        val token = call.parameters["token"]
        val data = call.receive<RegUser>()

        if (token == null){
            call.respond(HttpStatusCode.Unauthorized, "Auth token is null. Add it to headers")
        } else {
            repository.registrateUser(
                token,
                data
            )
                .whenFailure { t ->
                    call.respond(HttpStatusCode.BadRequest, t.message ?: "")
                }
                .whenSuccess {
                    call.respond("Успешно!")
                }
        }
    }

    post("api/v1/{token}/report") {
        val token = call.parameters["token"]
        val data = call.receive<ReportCreateRequest>()

        if (token == null) {
            call.respond(HttpStatusCode.Unauthorized, "Auth token is null. Add it to headers")
        } else {
            repository.createReport(
                token,
                data
            )
                .whenFailure { t ->
                    call.respond(HttpStatusCode.BadRequest, t.message ?: "")
                }
                .whenSuccess { report ->
                    call.respond(report)
                }
        }
    }
}