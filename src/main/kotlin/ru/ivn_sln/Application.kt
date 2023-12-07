package ru.ivn_sln

import io.ktor.server.application.*
import org.koin.core.Koin
import ru.ivn_sln.plugins.configKoin
import ru.ivn_sln.plugins.configureRouting
import ru.ivn_sln.plugins.configureSerialization
import ru.ivn_sln.plugins.connectRenderDatabase


fun Application.module() {
    configKoin()
    connectRenderDatabase()
    configureSerialization()
    configureRouting()
}
