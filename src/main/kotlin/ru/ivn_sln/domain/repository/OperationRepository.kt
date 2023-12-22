package ru.ivn_sln.domain.repository

import ru.ivn_sln.data.request.OperationInsertRequest
import ru.ivn_sln.data.request.OperationUpdateRequest
import ru.ivn_sln.data.request.ReportCreateRequest
import ru.ivn_sln.data.response.RegUser
import ru.ivn_sln.domain.models.Operation
import ru.ivn_sln.domain.models.OperationExtended
import ru.ivn_sln.tools.Resource

interface OperationRepository {

    suspend fun fetchOperations(token : String) : Resource<List<Operation>>

    suspend fun fetchFullOperation(operationId : Int) : Resource<OperationExtended>

    suspend fun deleteOperation(operationId: Int): Resource<Unit>

    suspend fun addOperation(
        token: String,
        operationInsertRequest: OperationInsertRequest
    ): Resource<Unit>

    suspend fun changeOperation(
        operationId: Int,
        operationUpdateRequest: OperationUpdateRequest,
    ): Resource<Unit>

    suspend fun registrateUser(
        token: String,
        userInfo: RegUser,
    ) : Resource<Unit>

    suspend fun createReport(
        token : String,
        reportCreateRequest: ReportCreateRequest,
    ) : Resource<Unit>
}