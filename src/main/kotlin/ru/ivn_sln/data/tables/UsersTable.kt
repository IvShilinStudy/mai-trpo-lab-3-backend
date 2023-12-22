package ru.ivn_sln.data.tables

import org.jetbrains.exposed.sql.Table

object UsersTable : Table("users") {
    val accountId = UsersTable
        .varchar("token", 100)
    val firstName = UsersTable
        .varchar("first_name", 50)
    val lastName = UsersTable
        .varchar("last_name", 50)
    val phone = UsersTable
        .varchar("phone", 11)

    override val primaryKey = PrimaryKey(accountId)
}