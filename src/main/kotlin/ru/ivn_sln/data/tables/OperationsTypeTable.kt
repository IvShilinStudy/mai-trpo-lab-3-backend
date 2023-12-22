package ru.ivn_sln.data.tables

import org.jetbrains.exposed.sql.Table

object OperationsTypeTable : Table("operations_type") {
    val id = OperationsTypeTable
        .integer("type_id")
        .autoIncrement()
    val name = OperationsTypeTable
        .varchar(
            "type_name",
            100
        )
   val coeff = OperationsTypeTable
       .double("type_coeff")

    override val primaryKey = PrimaryKey(id)
}