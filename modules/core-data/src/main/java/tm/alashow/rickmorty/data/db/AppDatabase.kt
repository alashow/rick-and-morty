/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.rickmorty.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import tm.alashow.domain.models.BaseTypeConverters
import tm.alashow.rickmorty.data.db.daos.CharactersDao
import tm.alashow.rickmorty.domain.entities.Character

@Database(
    version = 1,
    entities = [
        Character::class,
    ],
)
@TypeConverters(BaseTypeConverters::class, AppTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun charactersDao(): CharactersDao
}
