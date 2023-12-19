package ru.ivn_sln.data.tables

import org.jetbrains.exposed.sql.Table

object OperationsTypeTable : Table("operations_type") {
    val id = OperationsTypeTable
        .integer("id")
        .autoIncrement()
    val operationId = OperationsTypeTable
        .reference(
            "operation_id",
            OperationsTable.operation_id
        )
    val type = OperationsTypeTable
        .varchar(
            "type",
            50,
        )

    override val primaryKey = PrimaryKey(operationId)
}