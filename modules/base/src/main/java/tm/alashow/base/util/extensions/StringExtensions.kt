/*
 * Copyright (C) 2018, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.base.util.extensions

fun String?.orNA() = when (this.isNullOrEmpty()) {
    false -> this
    else -> "N/A"
}

fun String?.orBlank() = when (this == null) {
    false -> this
    else -> ""
}

fun List<String?>.interpunctize(interpunct: String = " ꞏ ") = joinToString(interpunct)
