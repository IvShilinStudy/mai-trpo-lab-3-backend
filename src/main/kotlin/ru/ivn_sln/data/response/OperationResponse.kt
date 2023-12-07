package ru.ivn_sln.data.response

import kotlinx.serialization.Serializable
import java.time.Instant

@Serializable
data class OperationResponse(
    val operationId: Int,
    val date: String,
    val sum: Int
)