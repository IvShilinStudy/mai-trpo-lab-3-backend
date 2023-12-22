package ru.ivn_sln.domain.models

import java.time.Instant

data class ReportCreate(
    val typeId: Int?,
    val categoryId: Int?,
    val fromDate: Instant,
    val toDate: Instant,
)