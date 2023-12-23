package ru.ivn_sln.data.data_source

import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import ru.ivn_sln.data.response.*
import ru.ivn_sln.data.tables.*
import ru.ivn_sln.domain.models.OperationInsert
import ru.ivn_sln.domain.models.OperationUpdate
import java.time.Instant
import kotlin.math.roundToInt

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

    override suspend fun createReportFromType(
        token: String,
        typeId: Int,
        fromDate: Instant,
        toDate: Instant
    ) : ReportInfo{
        return transaction {
            val operations = OperationsTable
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
                .select {
                    OperationsTable.accountId
                        .eq(token)
                        .and { OperationsTypeTable.id.eq(typeId) }
                        .and { OperationsTable.operationTimestamp.between(fromDate, toDate) }
                }
                .toList()
                .map { resultRow ->
                    OperationExtendedResponse(
                        operationId = resultRow[OperationsTable.operation_id],
                        date = resultRow[OperationsTable.operationTimestamp].toString(),
                        sum = resultRow[OperationsTable.sum],
                        category = OperationCategoryResponse(
                            id = resultRow[OperationsCategoryTable.id],
                            name = resultRow[OperationsCategoryTable.name],
                            coeff = resultRow[OperationsCategoryTable.coeff]
                        ),
                        type = OperationTypeResponse(
                            id = resultRow[OperationsTypeTable.id],
                            name = resultRow[OperationsTypeTable.name],
                            coeff = resultRow[OperationsTypeTable.coeff]
                        ),
                    )
                }

            val count = operations.count()

            val averageSum = operations.sumOf { it.sum } / count

            val sumOfComMap = mutableMapOf<Int, Int>()

            operations.forEach { operation ->
                val typeCoeff = operation.type.coeff
                val categoryCoeff = operation.category.coeff
                val finallyCoeff = typeCoeff * categoryCoeff
                val sumOfComForOperation = (operation.sum * finallyCoeff).roundToInt()

                sumOfComMap[operation.operationId] = sumOfComForOperation
            }

            val maxSumOfComPair = sumOfComMap.maxBy { it.value }.toPair()
            val minSumOfComPair = sumOfComMap.minBy { it.value }.toPair()

            OperationsReports.insert {
                it[accountId] = token
                it[this.averageSum] = averageSum
                it[mostProfitableOperationId] = maxSumOfComPair.first
                it[leastProfitableOperationId] = minSumOfComPair.first
                it[this.fromDate] = fromDate
                it[this.toDate] = toDate
                it[operationsCount] = count
            }

            ReportInfo(
                averageSum = averageSum,
                mostProfitableOperationId = maxSumOfComPair.first,
                leastProfitableOperationId = minSumOfComPair.first,
                count = count,
                fromDateString = fromDate.toString(),
                toDateString = toDate.toString(),
                id = 0,
            )
        }
    }

    override suspend fun createReportFromCategory(
        token: String,
        categoryId: Int,
        fromDate: Instant,
        toDate: Instant
    ) : ReportInfo{
        return transaction {
            val operations = OperationsTable
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
                .select {
                    OperationsTable.accountId
                        .eq(token)
                        .and { OperationsTypeTable.id.eq(categoryId) }
                        .and { OperationsTable.operationTimestamp.between(fromDate, toDate) }
                }
                .toList()
                .map { resultRow ->
                    OperationExtendedResponse(
                        operationId = resultRow[OperationsTable.operation_id],
                        date = resultRow[OperationsTable.operationTimestamp].toString(),
                        sum = resultRow[OperationsTable.sum],
                        category = OperationCategoryResponse(
                            id = resultRow[OperationsCategoryTable.id],
                            name = resultRow[OperationsCategoryTable.name],
                            coeff = resultRow[OperationsCategoryTable.coeff]
                        ),
                        type = OperationTypeResponse(
                            id = resultRow[OperationsTypeTable.id],
                            name = resultRow[OperationsTypeTable.name],
                            coeff = resultRow[OperationsTypeTable.coeff]
                        ),
                    )
                }

            val count = operations.count()
            val averageSum = operations.sumOf { it.sum } / count
            val sumOfComMap = mutableMapOf<Int, Int>()

            operations.forEach { operation ->
                val typeCoeff = operation.type.coeff
                val categoryCoeff = operation.category.coeff
                val finallyCoeff = typeCoeff * categoryCoeff
                val sumOfComForOperation = (operation.sum * finallyCoeff).roundToInt()

                sumOfComMap[operation.operationId] = sumOfComForOperation
            }

            val maxSumOfComPair = sumOfComMap.maxBy { it.value }.toPair()
            val minSumOfComPair = sumOfComMap.minBy { it.value }.toPair()

            OperationsReports.insert {
                it[accountId] = token
                it[this.averageSum] = averageSum
                it[mostProfitableOperationId] = maxSumOfComPair.first
                it[leastProfitableOperationId] = minSumOfComPair.first
                it[this.fromDate] = fromDate
                it[this.toDate] = toDate
                it[operationsCount] = count
            }

            ReportInfo(
                averageSum = averageSum,
                mostProfitableOperationId = maxSumOfComPair.first,
                leastProfitableOperationId = minSumOfComPair.first,
                count = count,
                fromDateString = fromDate.toString(),
                toDateString = toDate.toString(),
                id = 0,
            )
        }
    }

    override suspend fun fetchReports(token: String): List<ReportInfo> {
        return transaction {
            OperationsReports
                .select { OperationsReports.accountId.eq(token) }
                .toList()
                .map { row ->
                    ReportInfo(
                        averageSum = row[OperationsReports.averageSum],
                        mostProfitableOperationId = row[OperationsReports.mostProfitableOperationId],
                        leastProfitableOperationId = row[OperationsReports.leastProfitableOperationId],
                        count = row[OperationsReports.operationsCount],
                        fromDateString = row[OperationsReports.fromDate].toString(),
                        toDateString = row[OperationsReports.toDate].toString(),
                        id = row[OperationsReports.id],
                    )
                }
        }
    }
}