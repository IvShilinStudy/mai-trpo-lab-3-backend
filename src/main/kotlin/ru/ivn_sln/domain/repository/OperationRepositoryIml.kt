package ru.ivn_sln.domain.repository

import ru.ivn_sln.data.Operation

class OperationRepositoryIml : OperationRepository {

    override suspend fun fetchAllUserOperations(): List<Operation> {
        return emptyList()
    }
}