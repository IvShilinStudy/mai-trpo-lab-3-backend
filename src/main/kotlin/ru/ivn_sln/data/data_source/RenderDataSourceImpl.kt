package ru.ivn_sln.data.data_source

import org.jetbrains.exposed.sql.JoinType
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import ru.ivn_sln.data.response.OperationExtendedResponse
import ru.ivn_sln.data.response.OperationResponse
import ru.ivn_sln.data.tables.OperationDataTable
import ru.ivn_sln.data.tables.OperationTable

class RenderDataSourceImpl : RenderDataSource {
    override suspend fun fetchOperations(token: String): List<OperationResponse> {
        return transaction {
            OperationTable
                .select { OperationTable.account_id.eq(token) }
                .toList()
                .map { row ->
                    OperationResponse(
                        operationId = row[OperationTable.operation_id],
                        date = row[OperationTable.operation_data].toString(),
                        sum = row[OperationTable.sum],
                    )
                }
        }
    }

    override suspend fun fetchOperationsData(operationId: Int) : OperationExtendedResponse {
        return transaction {
            addLogger(StdOutSqlLogger)

            val operationRow = OperationTable
                .join(OperationDataTable, JoinType.INNER, OperationTable.operation_id, OperationDataTable.operationId)
                .select { OperationTable.operation_id.eq(operationId) }
                .firstOrNull() ?: throw IllegalStateException("Problems with join OperationDataTable and OperationTable")

            OperationExtendedResponse(
                operationId = operationRow[OperationTable.operation_id],
                date = operationRow[OperationTable.operation_data].toString(),
                sum = operationRow[OperationTable.sum],
                category = operationRow[OperationDataTable.category],
                sumOfCop = operationRow[OperationDataTable.sumOfCom],
            )
        }
    }
}