package ru.ivn_sln.data.request

import kotlinx.serialization.Serializable

@Serializable
data class OperationUpdateRequest(
    val date: String,
    val sum: Int,
    val categoryId: Int,
    val typeId: Int,
)