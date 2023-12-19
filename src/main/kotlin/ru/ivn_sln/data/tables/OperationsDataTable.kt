package ru.ivn_sln.data.tables

import org.jetbrains.exposed.sql.Table

object OperationsDataTable : Table("operations_data") {
    val id = OperationsDataTable
        .integer("id")
        .autoIncrement()
    val operationId = OperationsDataTable
        .reference(
            "operation_id",
            OperationsTable.operation_id
        )
    val category = OperationsDataTable
        .varchar("category", 20)
    val sumOfCom = OperationsDataTable
        .integer("sum_of_com")

    override val primaryKey = PrimaryKey(id)
}