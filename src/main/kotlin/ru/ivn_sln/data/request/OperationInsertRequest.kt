package ru.ivn_sln.data.request

import kotlinx.serialization.Serializable

@Serializable
data class OperationInsertRequest(
    val date: String,
    val sum: Int,
    val category: String,
    val sumOfCop: Int,
    val type: String,
)