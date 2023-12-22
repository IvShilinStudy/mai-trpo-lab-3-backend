package ru.ivn_sln.data.response

import kotlinx.serialization.Serializable

@Serializable
data class OperationTypeResponse(
    val id: Int,
    val name: String,
    val coeff: Double,
)