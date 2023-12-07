package ru.ivn_sln.domain.repository

import ru.ivn_sln.data.response.OperationExtendedResponse
import ru.ivn_sln.domain.models.Operation
import ru.ivn_sln.domain.models.OperationExtended
import ru.ivn_sln.tools.Resource

interface OperationRepository {

    suspend fun fetchOperations(token : String) : Resource<List<Operation>>

    suspend fun fetchFullOperation(operationId : Int) : Resource<OperationExtended>
}