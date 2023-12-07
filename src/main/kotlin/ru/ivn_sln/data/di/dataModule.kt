package ru.ivn_sln.data.di

import org.koin.dsl.module
import ru.ivn_sln.data.data_source.RenderDataSource
import ru.ivn_sln.data.data_source.RenderDataSourceImpl

fun dataModule() = module {
    single<RenderDataSource> { RenderDataSourceImpl() }
}