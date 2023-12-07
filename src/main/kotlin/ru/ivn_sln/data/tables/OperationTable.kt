package ru.ivn_sln.data.tables

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.timestamp

object OperationTable: Table("operations"){
    val operation_id = OperationTable.integer("operation_id")
    val account_id = OperationTable.varchar("account_id", 100)
    val operation_data = OperationTable.timestamp("operation_datetime")
    val sum = OperationTable.integer("sum")
}