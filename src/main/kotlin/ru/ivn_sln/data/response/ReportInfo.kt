package ru.ivn_sln.data.response

import kotlinx.serialization.Serializable

@Serializable
data class ReportInfo(
    val id: Int,
    val averageSum: Int,
    val mostProfitableOperationId: Int,
    val leastProfitableOperationId: Int,
    val count: Int,
    val fromDateString: String,
    val toDateString: String,
)
