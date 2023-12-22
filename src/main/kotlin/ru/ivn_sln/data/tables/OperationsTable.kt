package ru.ivn_sln.data.tables

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.timestamp

object OperationsTable : Table("operations") {
    val operation_id = OperationsTable
        .integer("operation_id")
        .autoIncrement()
    val accountId = OperationsTable
        .reference(
            name = "account_id",
            refColumn = UsersTable.accountId,
        )
    val operationTimestamp = OperationsTable
        .timestamp("operation_datetime")
    val sum = OperationsTable
        .integer("sum")
    val typeId = OperationsTable
        .reference(
            name = "type_id",
            refColumn = OperationsTypeTable.id,
        )
    val categoryId = OperationsTable
        .reference(
            name = "category_id",
            refColumn = OperationsCategoryTable.id,
        )

        override val primaryKey = PrimaryKey(operation_id)
}