package ru.ivn_sln.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Operation(
    val operationId : Int,
    val date : Long?,
    val sum: Int,
)