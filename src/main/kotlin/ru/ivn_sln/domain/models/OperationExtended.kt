package ru.ivn_sln.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class OperationExtended(
    val operationId: Int,
    val date: String,
    val sum: Int,
    val category: String?,
    val sumOfCop: Int?,
    val type: String?,
)