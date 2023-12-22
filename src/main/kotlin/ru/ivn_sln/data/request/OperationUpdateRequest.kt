package ru.ivn_sln.data.request

import kotlinx.serialization.Serializable

@Serializable
data class OperationUpdateRequest(
    val sum: Int,
    val categoryId: Int,
    val typeId: Int,
)