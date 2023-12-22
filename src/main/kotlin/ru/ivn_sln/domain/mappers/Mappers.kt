package ru.ivn_sln.domain.mappers

import ru.ivn_sln.data.request.OperationInsertRequest
import ru.ivn_sln.data.request.OperationUpdateRequest
import ru.ivn_sln.data.response.OperationCategoryResponse
import ru.ivn_sln.data.response.OperationExtendedResponse
import ru.ivn_sln.data.response.OperationResponse
import ru.ivn_sln.data.response.OperationTypeResponse
import ru.ivn_sln.domain.models.*
import java.time.Instant

fun OperationResponse.toDomainModel() =
    Operation(
        operationId = operationId,
        date = date,
        sum = sum,
    )

fun List<OperationResponse>.toDomainModel() =
    this.map { it.toDomainModel() }

fun OperationExtendedResponse.toDomainModel(sumOfCom: Int) =
    OperationExtended(
        operationId = operationId,
        date = date,
        sum = sum,
        category = category.toDomainModel(),
        sumOfCop = sumOfCom,
        type = type.toDomainModel(),
    )

fun OperationTypeResponse.toDomainModel() = OperationType(
    id = id,
    name = name,
)

fun OperationCategoryResponse.toDomainModel() = OperationCategory(
    id = id,
    name = name,
)

fun OperationInsertRequest.toDomainModel() = OperationInsert(
    date = Instant.parse(date),
    sum = sum,
    categoryId = categoryId,
    typeId = typeId,
)

fun OperationUpdateRequest.toDomainModel() = OperationUpdate(
    sum = sum,
    categoryId = categoryId,
    typeId = typeId,
)