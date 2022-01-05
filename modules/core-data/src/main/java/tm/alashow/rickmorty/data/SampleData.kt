/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.rickmorty.data

import tm.alashow.rickmorty.domain.entities.Character
import java.util.*
import kotlin.math.abs
import kotlin.random.Random

object SampleData {
    private val random = Random(1000)

    fun Random.id(): Long = abs(nextLong())
    fun Random.sid(): String = nextInt().toString()

    fun randomString() = UUID.randomUUID().toString().replace("-", "")

    val Character: Character = character()

    fun character() = Character(
        id = random.id(),
        primaryKey = "sample-character-${random.id()}",
        searchIndex = random.nextInt(),
        page = random.nextInt(),
        name = "Character ${random.id()}"
    )
}
