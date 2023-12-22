package ru.ivn_sln.data.data_source

import ru.ivn_sln.data.request.OperationInsertRequest
import ru.ivn_sln.data.request.OperationUpdateRequest
import ru.ivn_sln.data.response.OperationExtendedResponse
import ru.ivn_sln.data.response.OperationResponse
import ru.ivn_sln.data.response.RegUser
import ru.ivn_sln.domain.models.OperationExtended

interface RenderDataSource {
    suspend fun fetchOperations(token : String) : List<OperationResponse>

    suspend fun fetchOperationsData(operationId : Int) : OperationExtendedResponse

    suspend fun deleteOperation(operationId: Int)

    suspend fun insertNewOperation(
        token: String,
        operationInsertRequest: OperationInsertRequest,
    )

    suspend fun changeOperation(
        operationId : Int,
        operationUpdateRequest: OperationUpdateRequest,
    )

    suspend fun regUser(
        token: String,
        user: RegUser,
    )

    suspend fun getOperationsExtended(
        token: String,
        type: String,
        fromDate: String,
    ) : List<OperationExtendedResponse>
}