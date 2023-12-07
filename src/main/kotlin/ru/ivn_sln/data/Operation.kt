package ru.ivn_sln.data

import kotlinx.serialization.Serializable

@Serializable
data class Operation(
    val date : Long?,
    val sum : Int,
    val accountId: String,
    val operationId : Int
)