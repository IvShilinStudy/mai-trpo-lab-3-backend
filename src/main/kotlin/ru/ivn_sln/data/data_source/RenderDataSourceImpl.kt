package ru.ivn_sln.data.data_source

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import ru.ivn_sln.data.request.OperationInsertRequest
import ru.ivn_sln.data.request.OperationUpdateRequest
import ru.ivn_sln.data.response.OperationExtendedResponse
import ru.ivn_sln.data.response.OperationResponse
import ru.ivn_sln.data.response.RegUser
import ru.ivn_sln.data.tables.*
import java.time.Instant

class RenderDataSourceImpl : RenderDataSource {
    override suspend fun fetchOperations(token: String): List<OperationResponse> {
        return transaction {
            addLogger(StdOutSqlLogger)

            OperationsTable
                .select { OperationsTable.account_id.eq(token) }
                .toList()
                .map { row ->
                    OperationResponse(
                        operationId = row[OperationsTable.operation_id],
                        date = row[OperationsTable.operation_timestamp].toString(),
                        sum = row[OperationsTable.sum],
                    )
                }
        }
    }

    override suspend fun fetchOperationsData(operationId: Int) : OperationExtendedResponse {
        return transaction {
            addLogger(StdOutSqlLogger)

            val operationRow = OperationsTable
                .join(
                    OperationsDataTable,
                    JoinType.LEFT,
                    OperationsTable.operation_id,
                    OperationsDataTable.operationId
                )
                .join(
                    OperationsTypeTable,
                    JoinType.LEFT,
                    OperationsTable.operation_id,
                    OperationsTypeTable.operationId
                )
                .select { OperationsTable.operation_id.eq(operationId) }
                .firstOrNull() ?: throw Throwable("Проблемы с операцией")

            OperationExtendedResponse(
                operationId = operationRow[OperationsTable.operation_id],
                date = operationRow[OperationsTable.operation_timestamp].toString(),
                sum = operationRow[OperationsTable.sum],
                category = operationRow[OperationsDataTable.category],
                sumOfCop = operationRow[OperationsDataTable.sumOfCom],
                type = operationRow[OperationsTypeTable.type],
            )
        }
    }

    override suspend fun deleteOperation(operationId: Int) {
        transaction {
            addLogger(StdOutSqlLogger)

            val operationRow = OperationsTable.select {
                OperationsTable.operation_id.eq(operationId)
            }.firstOrNull() ?: throw Throwable("Проблемы с операцией")

            OperationsTable.deleteWhere {
                OperationsTable.operation_id.eq(operationId)
            }

            OperationsArchive.insert {
                it[OperationsArchive.operation_id] = operationRow[OperationsTable.operation_id]
                it[OperationsArchive.account_id] = operationRow[OperationsTable.account_id]
                it[OperationsTable.operation_timestamp] = operationRow[OperationsTable.operation_timestamp]
                it[OperationsArchive.sum] = operationRow[OperationsTable.sum]
            }
        }
    }

    override suspend fun insertNewOperation(
        token : String,
        operationInsertRequest: OperationInsertRequest
    ) {
        transaction {
            addLogger(StdOutSqlLogger)

            val insertedRow = OperationsTable.insert {
                it[OperationsTable.account_id] = token
                it[OperationsTable.sum] = operationInsertRequest.sum
                it[OperationsTable.operation_timestamp] = Instant.parse(operationInsertRequest.date)
            }.resultedValues?.firstOrNull() ?: throw Throwable("Не получилось добавить операцию. Попробуйте еще раз")

            OperationsDataTable.insert {
                it[OperationsDataTable.operationId] = insertedRow[OperationsTable.operation_id]
                it[OperationsDataTable.sumOfCom] = operationInsertRequest.sumOfCop
                it[OperationsDataTable.category] = operationInsertRequest.category
            }

            OperationsTypeTable.insert {
                it[OperationsTypeTable.operationId] = insertedRow[OperationsTable.operation_id]
                it[OperationsTypeTable.type] = operationInsertRequest.type
            }
        }
    }

    override suspend fun changeOperation(
        operationId : Int,
        operationUpdateRequest: OperationUpdateRequest
    ) {
        transaction {
            addLogger(StdOutSqlLogger)

            OperationsTable.update(
                where = {
                    OperationsTable.operation_id.eq(operationId)
                },
                body = {
                    it[OperationsTable.operation_timestamp] = Instant.parse(operationUpdateRequest.date)
                    it[OperationsTable.sum] = operationUpdateRequest.sum
                }
            )

            OperationsDataTable.update(
                where = {
                    OperationsDataTable.operationId.eq(operationId)
                },
                body = {
                    it[OperationsDataTable.sumOfCom] = operationUpdateRequest.sumOfCop
                    it[OperationsDataTable.category] = operationUpdateRequest.category
                }
            )
        }
    }

    override suspend fun regUser(
        token: String,
        user: RegUser,
    ) {
        transaction {
            addLogger(StdOutSqlLogger)

            UsersTable.insert {
                it[accountId] = token
                it[firstName] = user.firstName
                it[lastName] = user.lastName
                it[phone] = user.phone
            }
        }
    }
}