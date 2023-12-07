package ru.ivn_sln.di

import org.koin.core.context.startKoin
import org.koin.dsl.module
import ru.ivn_sln.domain.repository.OperationRepository
import ru.ivn_sln.domain.repository.OperationRepositoryIml

fun appModule() = module {
    single<OperationRepository> { OperationRepositoryIml() }
}

fun configKoin(){
    startKoin { listOf(appModule()) }
}