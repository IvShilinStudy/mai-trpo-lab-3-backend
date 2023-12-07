package ru.ivn_sln.data.tables

import org.jetbrains.exposed.sql.Table

object OperationDataTable : Table("operation_data") {
    val id = integer("id").autoIncrement()
    val operationId = reference("operation_id", OperationTable.operation_id)
    val category = varchar("category", 20)
    val sumOfCom = integer("sum_of_com")

    override val primaryKey = PrimaryKey(id)
}