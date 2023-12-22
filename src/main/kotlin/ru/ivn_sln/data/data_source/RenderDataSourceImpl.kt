package ru.ivn_sln.data.data_source

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import ru.ivn_sln.data.request.OperationInsertRequest
import ru.ivn_sln.data.request.OperationUpdateRequest
import ru.ivn_sln.data.response.*
import ru.ivn_sln.data.tables.*
import ru.ivn_sln.domain.models.OperationInsert
import ru.ivn_sln.domain.models.OperationUpdate
import java.time.Instant

class RenderDataSourceImpl : RenderDataSource {
    override suspend fun fetchOperations(token: String): List<OperationResponse> {
        return transaction {
            addLogger(StdOutSqlLogger)

            OperationsTable
                .select { OperationsTable.accountId.eq(token) }
                .toList()
                .map { row ->
                    OperationResponse(
                        operationId = row[OperationsTable.operation_id],
                        date = row[OperationsTable.operationTimestamp].toString(),
                        sum = row[OperationsTable.sum],
                    )
                }
        }
    }

    override suspend fun fetchOperationsData(operationId: Int): OperationExtendedResponse {
        return transaction {
            addLogger(StdOutSqlLogger)

            val operationExtendedRaw = OperationsTable
                .join(
                    OperationsCategoryTable,
                    JoinType.LEFT,
                    OperationsTable.categoryId,
                    OperationsCategoryTable.id,
                )
                .join(
                    OperationsTypeTable,
                    JoinType.LEFT,
                    OperationsTable.typeId,
                    OperationsTypeTable.id,
                )
                .select { OperationsTable.operation_id.eq(operationId) }
                .firstOrNull() ?: throw Throwable("Проблемы с операцией")

            OperationExtendedResponse(
                operationId = operationExtendedRaw[OperationsTable.operation_id],
                date = operationExtendedRaw[OperationsTable.operationTimestamp].toString(),
                sum = operationExtendedRaw[OperationsTable.sum],
                category = OperationCategoryResponse(
                    id = operationExtendedRaw[OperationsCategoryTable.id],
                    name = operationExtendedRaw[OperationsCategoryTable.name],
                    coeff = operationExtendedRaw[OperationsCategoryTable.coeff]
                ),
                type = OperationTypeResponse(
                    id = operationExtendedRaw[OperationsTypeTable.id],
                    name = operationExtendedRaw[OperationsTypeTable.name],
                    coeff = operationExtendedRaw[OperationsTypeTable.coeff]
                ),
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
                operation_id.eq(operationId)
            }

            OperationsArchive.insert {
                it[operation_id] = operationRow[OperationsTable.operation_id]
                it[account_id] = operationRow[OperationsTable.accountId]
                it[operation_timestamp] = operationRow[OperationsTable.operationTimestamp]
                it[sum] = operationRow[OperationsTable.sum]
            }
        }
    }

    override suspend fun insertNewOperation(
        token : String,
        operationInsert: OperationInsert
    ) {
        transaction {
            addLogger(StdOutSqlLogger)

            OperationsTable.insert {
                it[accountId] = token
                it[sum] = operationInsert.sum
                it[operationTimestamp] = operationInsert.date
                it[typeId] = operationInsert.typeId
                it[categoryId] = operationInsert.categoryId
            }
        }
    }

    override suspend fun changeOperation(
        operationId : Int,
        operationUpdate: OperationUpdate
    ) {
        transaction {
            addLogger(StdOutSqlLogger)

            OperationsTable.update(
                where = {
                    OperationsTable.operation_id.eq(operationId)
                },
                body = {
                    it[sum] = operationUpdate.sum
                    it[typeId] = operationUpdate.typeId
                    it[categoryId] = operationUpdate.categoryId
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