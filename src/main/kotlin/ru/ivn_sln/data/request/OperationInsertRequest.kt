package ru.ivn_sln.data.request

import kotlinx.serialization.Serializable

@Serializable
data class OperationInsertRequest(
    val date: String? = null,
    val sum: Int,
    val category: String,
    val sumOfCop: Int,
)