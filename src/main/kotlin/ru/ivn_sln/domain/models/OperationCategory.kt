package ru.ivn_sln.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class OperationCategory(
    val id: Int,
    val name: String,
)