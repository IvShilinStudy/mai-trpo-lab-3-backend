package ru.ivn_sln.domain.repository

import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import ru.ivn_sln.data.Operation
import ru.ivn_sln.data.tables.OperationTable

class OperationRepositoryIml : OperationRepository {

    override suspend fun fetchAllUserOperations(accountId: String): List<Operation> {
        var list = emptyList<Operation>()
        transaction {
            val operationTableRequest = OperationTable.select {
                OperationTable.account_id.eq(accountId)
            }.toList()

            list = operationTableRequest.map { row ->
                Operation(
                    (row[OperationTable.operation_data])?.toEpochMilli(),
                    row[OperationTable.sum],
                    row[OperationTable.account_id],
                    row[OperationTable.operation_id],
                )
            }
        }

        return list
    }
}