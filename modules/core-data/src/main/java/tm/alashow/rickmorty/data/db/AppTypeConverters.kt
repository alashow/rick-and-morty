/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.rickmorty.data.db

import androidx.room.TypeConverter
import kotlinx.serialization.builtins.ListSerializer
import tm.alashow.rickmorty.data.CharacterParams
import tm.alashow.rickmorty.data.SearchParams
import tm.alashow.rickmorty.domain.entities.Character
import tm.alashow.domain.models.DEFAULT_JSON_FORMAT

object AppTypeConverters {

    private val json = DEFAULT_JSON_FORMAT

    @TypeConverter
    @JvmStatic
    fun fromCharacterParams(params: CharacterParams) = params.toString()

    @TypeConverter
    @JvmStatic
    fun fromSearchParams(params: SearchParams) = params.toString()

    @TypeConverter
    @JvmStatic
    fun toCharacterList(value: String): List<Character> = json.decodeFromString(ListSerializer(Character.serializer()), value)

    @TypeConverter
    @JvmStatic
    fun fromCharacterList(value: List<Character>): String = json.encodeToString(ListSerializer(Character.serializer()), value)
}
