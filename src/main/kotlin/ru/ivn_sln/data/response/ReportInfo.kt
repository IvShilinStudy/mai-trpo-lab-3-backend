package ru.ivn_sln.data.response

import kotlinx.serialization.Serializable

@Serializable
data class ReportInfo(
    val averageSum: Int,
    val mostProfitableOperationId: Int,
    val leastProfitableOperationId: Int,
    val count: Int,
)
