package ru.ivn_sln.data.tables

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.timestamp

object OperationsReports : Table("operations_report") {
    val id = integer("operation_report_id")
        .autoIncrement()
    val accountId = reference(
        name = "account_id",
        refColumn = UsersTable.accountId
    )
    val averageSum = integer("average_sum")
    val mostProfitableOperationId = reference(
        name = "most_profitable_operation_id",
        refColumn = OperationsTable.operation_id
    )
    val leastProfitableOperationId = reference(
        name = "least_profitable_operation_id",
        refColumn = OperationsTable.operation_id
    )
    val fromDate = timestamp("from_date")
    val toDate = timestamp("to_date")
    val operationsCount = integer("operation_count")

    override val primaryKey = PrimaryKey(id)
}