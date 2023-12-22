package ru.ivn_sln.domain.repository

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.ivn_sln.data.data_source.RenderDataSource
import ru.ivn_sln.data.request.OperationInsertRequest
import ru.ivn_sln.data.request.OperationUpdateRequest
import ru.ivn_sln.data.request.ReportCreateRequest
import ru.ivn_sln.data.response.RegUser
import ru.ivn_sln.domain.CalculateOperationSumOfComUseCase
import ru.ivn_sln.domain.mappers.toDomainModel
import ru.ivn_sln.tools.resource

class OperationRepositoryIml : OperationRepository, KoinComponent {

    private val dataSource : RenderDataSource by inject()
    private val calculateOperationSumOfComUseCase: CalculateOperationSumOfComUseCase by inject()

    override suspend fun fetchOperations(token: String) = resource {
        dataSource.fetchOperations(token).toDomainModel()
    }

    override suspend fun fetchFullOperation(operationId: Int) = resource {
        val operationExtended = dataSource.fetchOperationsData(operationId)
        val sumOfCom = calculateOperationSumOfComUseCase(operationExtended)

        operationExtended.toDomainModel(sumOfCom)
    }

    override suspend fun deleteOperation(operationId: Int) = resource {
        dataSource.deleteOperation(operationId)
    }

    override suspend fun registrateUser(
        token: String,
        userInfo: RegUser
    ) = resource {
        dataSource.regUser(
            token,
            userInfo,
        )
    }

    override suspend fun addOperation(
        token: String,
        operationInsertRequest: OperationInsertRequest
    ) = resource {
        val operationInsert = operationInsertRequest.toDomainModel()

        dataSource.insertNewOperation(
            token,
            operationInsert
        )
    }

    override suspend fun changeOperation(
        operationId : Int,
        operationUpdateRequest: OperationUpdateRequest
    ) = resource {
        val operationUpdate = operationUpdateRequest.toDomainModel()

        dataSource.changeOperation(
            operationId,
            operationUpdate
        )
    }

    override suspend fun createReport(
        token: String,
        reportCreateRequest: ReportCreateRequest
    ) = resource {
        val reportCreate = reportCreateRequest.toDomainModel()
        val typeId = reportCreate.typeId
        val categoryId = reportCreate.categoryId



        when {
            typeId != null && categoryId == null -> {
                dataSource.createReportFromType(
                    token =  token,
                    typeId =  typeId,
                    fromDate = reportCreate.fromDate,
                    toDate = reportCreate.toDate
                )
            }
            categoryId != null && typeId == null -> {
                dataSource.createReportFromCategory(
                    token =  token,
                    categoryId =  categoryId,
                    fromDate = reportCreate.fromDate,
                    toDate = reportCreate.toDate,
                )
            }
            else -> {
                throw Throwable("Проверьте ваш запрос")
            }
        }
    }
}