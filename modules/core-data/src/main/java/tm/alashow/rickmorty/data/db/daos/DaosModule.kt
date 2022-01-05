/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.rickmorty.data.db.daos

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import tm.alashow.data.db.PaginatedEntryDao
import tm.alashow.rickmorty.data.SearchParams
import tm.alashow.rickmorty.data.db.AppDatabase
import tm.alashow.rickmorty.domain.entities.Character

@InstallIn(SingletonComponent::class)
@Module
class DaosModule {

    @Provides
    fun charactersDao(db: AppDatabase) = db.charactersDao()

    @Provides
    fun charactersDaoBase(db: AppDatabase): PaginatedEntryDao<SearchParams, Character> = db.charactersDao()
}
