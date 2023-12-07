package ru.ivn_sln.domain.mappers

import ru.ivn_sln.data.response.OperationExtendedResponse
import ru.ivn_sln.data.response.OperationResponse
import ru.ivn_sln.domain.models.Operation
import ru.ivn_sln.domain.models.OperationExtended

fun OperationResponse.toDomainModel() =
    Operation(
        operationId = operationId,
        date = null,
        sum = sum,
    )

fun List<OperationResponse>.toDomainModel() =
    this.map { it.toDomainModel() }

fun OperationExtendedResponse.toDomainModel() =
    OperationExtended(
        operationId = operationId,
        date = null,
        sum = sum,
        category = category,
        sumOfCop = sumOfCop,
    )