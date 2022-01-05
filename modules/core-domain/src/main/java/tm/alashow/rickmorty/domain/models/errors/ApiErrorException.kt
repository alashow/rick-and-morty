/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.rickmorty.domain.models.errors

import androidx.annotation.StringRes

open class ApiErrorException(
    override val message: String? = null,

    @StringRes
    open val errorRes: Int? = null
) : RuntimeException("API returned an error: $message") {
    override fun toString() = message ?: super.toString()
}
