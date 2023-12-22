package ru.ivn_sln.domain.models

import java.time.Instant

data class OperationInsert(
    val date: Instant,
    val sum: Int,
    val categoryId: Int,
    val typeId: Int,
)