package ru.ivn_sln.domain.di

import org.koin.dsl.module
import ru.ivn_sln.domain.CalculateOperationSumOfComUseCase
import ru.ivn_sln.domain.repository.OperationRepository
import ru.ivn_sln.domain.repository.OperationRepositoryIml

fun domainModule() = module {
    single<OperationRepository> { OperationRepositoryIml() }
    factory { CalculateOperationSumOfComUseCase() }
}