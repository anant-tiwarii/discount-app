package io.nexure.discount.util

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject


@Suppress("unused")
inline fun <reified T : Any> getKoinInstance(): T {
    return object : KoinComponent {
        val value: T by inject()
    }.value
}