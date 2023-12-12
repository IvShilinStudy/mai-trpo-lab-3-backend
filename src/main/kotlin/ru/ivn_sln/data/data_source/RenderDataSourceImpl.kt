package ru.ivn_sln.data.data_source

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import ru.ivn_sln.data.request.OperationInsertRequest
import ru.ivn_sln.data.request.OperationUpdateRequest
import ru.ivn_sln.data.response.OperationExtendedResponse
import ru.ivn_sln.data.response.OperationResponse
import ru.ivn_sln.data.tables.OperationDataTable
import ru.ivn_sln.data.tables.OperationTable

class RenderDataSourceImpl : RenderDataSource {
    override suspend fun fetchOperations(token: String): List<OperationResponse> {
        return transaction {
            addLogger(StdOutSqlLogger)

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

    override suspend fun deleteOperation(operationId: Int) {
        transaction {
            addLogger(StdOutSqlLogger)

            OperationTable.deleteWhere { OperationTable.operation_id.eq(operationId) }
            OperationDataTable.deleteWhere { OperationDataTable.operationId.eq(operationId) }
        }
    }

    override suspend fun insertNewOperation(
        token : String,
        operationInsertRequest: OperationInsertRequest
    ) {
        transaction {
            addLogger(StdOutSqlLogger)

            val insertedRow = OperationTable.insert {
                it[OperationTable.account_id] = token
                it[OperationTable.sum] = operationInsertRequest.sum
                it[OperationTable.operation_data] = null
            }.resultedValues?.firstOrNull() ?: throw Throwable("Не получилось добавить операцию. Попробуйте еще раз")

            OperationDataTable.insert {
                it[OperationDataTable.operationId] = insertedRow[OperationTable.operation_id]
                it[OperationDataTable.sumOfCom] = operationInsertRequest.sumOfCop
                it[OperationDataTable.category] = operationInsertRequest.category
            }
        }
    }

    override suspend fun changeOperation(
        operationId : Int,
        operationUpdateRequest: OperationUpdateRequest
    ) {
        transaction {
            addLogger(StdOutSqlLogger)

            OperationTable.update(
                where = {
                    OperationTable.operation_id.eq(operationId)
                },
                body = {
                    it[OperationTable.operation_data] = null
                    it[OperationTable.sum] = operationUpdateRequest.sum
                }
            )

            OperationDataTable.update(
                where = {
                    OperationDataTable.operationId.eq(operationId)
                },
                body = {
                    it[OperationDataTable.sumOfCom] = operationUpdateRequest.sumOfCop
                    it[OperationDataTable.category] = operationUpdateRequest.category
                }
            )
        }
    }
}