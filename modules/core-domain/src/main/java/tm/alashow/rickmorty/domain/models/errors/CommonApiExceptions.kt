/*
 * Copyright (C) 2018, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.rickmorty.domain.models.errors

class NotFoundException(override val message: String = "Not found") : ApiErrorException(message)
class EmptyResultException(override val message: String = "Result was empty") : ApiErrorException(message)

fun <T> List<T>?.throwOnEmpty() = if (isNullOrEmpty()) throw EmptyResultException() else this

fun <T> Result<List<T>>.requireNonEmpty(condition: () -> Boolean = { true }): List<T> {
    return getOrThrow().apply { if (condition()) throwOnEmpty() }
}
