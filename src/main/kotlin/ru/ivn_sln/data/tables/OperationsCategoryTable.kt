package ru.ivn_sln.data.tables

import org.jetbrains.exposed.sql.Table

object OperationsCategoryTable : Table("operations_category") {
    val id = OperationsCategoryTable
        .integer("category_id")
        .autoIncrement()
    val name = OperationsCategoryTable
        .varchar(
            "category_name",
            100
        )
    val coeff = OperationsCategoryTable
        .double("category_coeff")

    override val primaryKey = PrimaryKey(id)
}