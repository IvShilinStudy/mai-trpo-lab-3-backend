package ru.ivn_sln.domain.repository

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.ivn_sln.data.data_source.RenderDataSource
import ru.ivn_sln.data.request.OperationInsertRequest
import ru.ivn_sln.data.request.OperationUpdateRequest
import ru.ivn_sln.data.response.RegUser
import ru.ivn_sln.domain.mappers.toDomainModel
import ru.ivn_sln.tools.resource

class OperationRepositoryIml : OperationRepository, KoinComponent {

    private val dataSource : RenderDataSource by inject()

    override suspend fun fetchOperations(token: String) = resource {
        dataSource.fetchOperations(token).toDomainModel()
    }

    override suspend fun fetchFullOperation(operationId: Int) = resource {
        dataSource.fetchOperationsData(operationId).toDomainModel()
    }

    override suspend fun deleteOperation(operationId: Int) = resource {
        dataSource.deleteOperation(operationId)
    }

    override suspend fun addOperation(
        token: String,
        operationInsertRequest: OperationInsertRequest
    ) = resource {
        dataSource.insertNewOperation(
            token,
            operationInsertRequest
        )
    }

    override suspend fun changeOperation(
        operationId : Int,
        operationUpdateRequest: OperationUpdateRequest
    ) = resource {
        dataSource.changeOperation(
            operationId,
            operationUpdateRequest
        )
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

    override suspend fun recept(
        token: String,
        type: String,
        fromDate: String
    ) = resource {
        dataSource.getOperationsExtended(
            token,
            type,
            fromDate
        ).toDomainModel()
    }
}