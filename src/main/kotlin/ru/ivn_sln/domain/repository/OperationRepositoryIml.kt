package ru.ivn_sln.domain.repository

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.ivn_sln.data.data_source.RenderDataSource
import ru.ivn_sln.domain.mappers.toDomainModel
import ru.ivn_sln.domain.models.OperationExtended
import ru.ivn_sln.tools.Resource
import ru.ivn_sln.tools.resource

class OperationRepositoryIml : OperationRepository, KoinComponent {

    private val dataSource : RenderDataSource by inject()

    override suspend fun fetchOperations(token: String) = resource {
        dataSource.fetchOperations(token).toDomainModel()
    }

    override suspend fun fetchFullOperation(operationId: Int) = resource {
        dataSource.fetchOperationsData(operationId).toDomainModel()
    }
}