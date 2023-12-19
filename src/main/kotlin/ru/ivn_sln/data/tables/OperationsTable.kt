package ru.ivn_sln.data.tables

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.timestamp

object OperationsTable : Table("operations") {
    val operation_id = OperationsTable
        .integer("operation_id")
        .autoIncrement()
    val account_id = OperationsTable
        .reference(
            name = "account_id",
            refColumn = UsersTable.accountId,
        )
    val operation_timestamp = OperationsTable
        .timestamp("operation_datetime")
    val sum = OperationsTable
        .integer("sum")

    override val primaryKey = PrimaryKey(operation_id)
}