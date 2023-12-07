package ru.ivn_sln.domain.repository

import ru.ivn_sln.data.Operation

interface OperationRepository {

    suspend fun fetchAllUserOperations(accountId : String) : List<Operation>
}