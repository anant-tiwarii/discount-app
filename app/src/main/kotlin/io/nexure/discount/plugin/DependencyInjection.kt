package io.nexure.discount.plugin

import io.ktor.server.application.Application
import io.nexure.discount.repository.repositoryModule
import io.nexure.discount.service.serviceModule
import org.kodein.di.ktor.di


fun Application.configureDependencyInjection() {
    di {
        import(repositoryModule)
        import(serviceModule)
    }
}
