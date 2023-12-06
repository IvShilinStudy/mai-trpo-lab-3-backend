package ru.ivn_sln.data

import kotlinx.serialization.Serializable

@Serializable
data class Operation(
    val id: Int,
    val date: String,
    val sum: Int,
)