package io.nexure.discount.plugin

import io.ktor.server.application.Application
import io.nexure.discount.model.cache.VatCache
import io.nexure.discount.repository.VatRepository
import kotlinx.coroutines.runBlocking
import org.kodein.di.instance
import org.kodein.di.ktor.closestDI
import org.slf4j.LoggerFactory

fun Application.configureCache() {

    val log = LoggerFactory.getLogger("CacheConfig")

    log.info("Loading VAT cache from database")

    val vatRepository by closestDI().instance<VatRepository>()

    runBlocking {
        runCatching {
            val vats = vatRepository.loadAll()
            VatCache.putAll(vats)
        }.onSuccess {
            log.info("VAT cache loaded successfully")
        }.onFailure { ex ->
            log.error("Failed to load VAT cache", ex)
        }
    }
}