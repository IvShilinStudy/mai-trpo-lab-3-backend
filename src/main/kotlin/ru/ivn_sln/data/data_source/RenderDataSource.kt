package ru.ivn_sln.data.data_source

import ru.ivn_sln.data.response.OperationExtendedResponse
import ru.ivn_sln.data.response.OperationResponse

interface RenderDataSource {
    suspend fun fetchOperations(token : String) : List<OperationResponse>

    suspend fun fetchOperationsData(operationId : Int) : OperationExtendedResponse
}