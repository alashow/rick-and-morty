/*
 * Copyright (C) 2018, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.domain.extensions

fun String?.orNA(ifBlank: Boolean = false, na: String = "N/A") = when (this.isNullOrEmpty() || (ifBlank && this.isBlank())) {
    false -> this
    else -> na
}

fun String?.orBlank() = when (this == null) {
    false -> this
    else -> ""
}

fun List<String?>.interpunctize(interpunct: String = " ꞏ ") = filter { !it.isNullOrBlank() }
    .joinToString(interpunct)
