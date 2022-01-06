/*
 * Copyright (C) 2018, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.domain.extensions

import java.util.*
import kotlin.math.max

inline val <T : Any> T.simpleName: String get() = this.javaClass.kotlin.simpleName ?: "Unknown"

fun randomUUID(): String = UUID.randomUUID().toString()
fun Boolean.toFloat() = if (this) 1f else 0f

infix fun Float.muteUntil(that: Float) = max(this - that, 0.0f) * (1 / (1 - that))
