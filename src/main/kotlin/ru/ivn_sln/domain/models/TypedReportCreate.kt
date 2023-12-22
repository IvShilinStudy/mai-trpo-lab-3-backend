package ru.ivn_sln.domain.models

import java.time.Instant

data class TypedReportCreate(
    val typeId : Int?,
    val fromDate: Instant,
    val toDate: Instant,
)