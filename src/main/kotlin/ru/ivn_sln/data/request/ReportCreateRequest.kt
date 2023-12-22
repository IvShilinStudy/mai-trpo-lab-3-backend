package ru.ivn_sln.data.request

import kotlinx.serialization.Serializable

@Serializable
data class ReportCreateRequest(
    val categoryId: Int? = null,
    val typeId: Int? = null,
    val fromDateString: String,
    val toDateString: String,
)