package ru.ivn_sln.data.tables

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.timestamp

object OperationsArchive : Table("operations_archive") {
    val id = OperationsArchive
        .integer("id")
        .autoIncrement()
    val operation_id = OperationsArchive
        .integer("operation_id")
    val account_id = OperationsArchive
        .reference(
            name = "account_id",
            refColumn = UsersTable.accountId,
        )
    val operation_timestamp = OperationsArchive
        .timestamp("operation_datetime")
    val sum = OperationsArchive
        .integer("sum")

    override val primaryKey = PrimaryKey(id)
}