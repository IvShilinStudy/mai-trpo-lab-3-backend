package ru.ivn_sln.data.data_source

import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import ru.ivn_sln.data.response.OperationResponse
import ru.ivn_sln.data.tables.OperationTable

class RenderDataSourceImpl : RenderDataSource {
    override suspend fun fetchOperations(token: String): List<OperationResponse> {
        return transaction {
            OperationTable
                .select { OperationTable.account_id.eq(token) }
                .toList()
                .map { row ->
                    OperationResponse(
                        row[OperationTable.operation_id],
                        row[OperationTable.operation_data].toString(),
                        row[OperationTable.sum],
                    )
                }
        }
    }

    override suspend fun fetchOperationsData(operationId: Int) {
        TODO("Not yet implemented")
    }
}