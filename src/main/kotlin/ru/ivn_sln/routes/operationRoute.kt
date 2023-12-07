package ru.ivn_sln.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.ktor.ext.inject
import ru.ivn_sln.domain.repository.OperationRepository
import ru.ivn_sln.domain.repository.OperationRepositoryIml

fun Route.operationRoute() {
//    val repository : OperationRepository by inject()

    get("api/v1/operation/info") {
        val repository : OperationRepository = OperationRepositoryIml()

        launch(Dispatchers.IO) {
            val accountId = call.request.headers["token"]
            if (accountId == null){
                call.respond(HttpStatusCode.Unauthorized, "Auth token")
            } else {
                val response = repository.fetchAllUserOperations(accountId)
                call.respond(response)
            }
        }
    }
}