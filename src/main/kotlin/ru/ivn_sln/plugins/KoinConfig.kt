package ru.ivn_sln.plugins

import org.koin.core.context.startKoin
import ru.ivn_sln.data.di.dataModule
import ru.ivn_sln.domain.di.domainModule

fun configKoin(){
    startKoin { modules(appModules()) }
}

fun appModules() = listOf(
    dataModule(),
    domainModule(),
)