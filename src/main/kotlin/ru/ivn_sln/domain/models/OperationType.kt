package ru.ivn_sln.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class OperationType(
    val id: Int,
    val name: String,
)
