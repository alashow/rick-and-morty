/*
 * Copyright (C) 2022, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.rickmorty.domain

const val UNKNOWN_ITEM = "unknown"

fun String?.isUnknown(): Boolean = this?.lowercase() == UNKNOWN_ITEM
