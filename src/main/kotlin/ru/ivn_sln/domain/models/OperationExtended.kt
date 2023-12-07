package ru.ivn_sln.domain.models

data class OperationExtended(
    val operationId: Int,
    val date: String?,
    val sum: Int,
    val category: String,
    val sumOfCop: Int,
)