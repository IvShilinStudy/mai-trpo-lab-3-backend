package ru.ivn_sln.domain.mappers

import ru.ivn_sln.data.response.OperationResponse
import ru.ivn_sln.domain.models.Operation

fun OperationResponse.toDomainModel() =
    Operation(
        operationId = operationId,
        date = null,
        sum = sum,
    )

fun List<OperationResponse>.toDomainModel() =
    this.map { it.toDomainModel() }