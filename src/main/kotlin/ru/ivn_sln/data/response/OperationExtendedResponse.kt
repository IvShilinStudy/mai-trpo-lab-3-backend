package ru.ivn_sln.data.response

data class OperationExtendedResponse(
    val operationId: Int,
    val date: String,
    val sum: Int,
    val category: String,
    val sumOfCop: Int,
)