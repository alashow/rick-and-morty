/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.rickmorty.data.repos.character

import com.dropbox.android.external.store4.Fetcher
import com.dropbox.android.external.store4.SourceOfTruth
import com.dropbox.android.external.store4.Store
import com.dropbox.android.external.store4.StoreBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import tm.alashow.data.LastRequests
import tm.alashow.data.PreferencesStore
import tm.alashow.rickmorty.data.CharacterParams
import tm.alashow.rickmorty.data.db.daos.CharactersDao
import tm.alashow.rickmorty.domain.entities.Character
import javax.inject.Named
import javax.inject.Singleton

typealias CharacterDetailsStore = Store<CharacterParams, Character>

@InstallIn(SingletonComponent::class)
@Module
object CharacterDetailsStoreModule {

    private suspend fun <T> Result<T>.fetcherDefaults(lastRequests: LastRequests, params: CharacterParams) = onSuccess {
        if (params.page == 0)
            lastRequests.save(params.toString())
    }.getOrThrow()

    private fun Flow<Character?>.sourceReaderFilter(lastRequests: LastRequests, params: CharacterParams) = map { entry ->
        when (entry != null) {
            true -> {
                when {
                    lastRequests.isExpired(params.toString()) -> null
                    else -> entry
                }
            }
            else -> null
        }
    }

    @Provides
    @Singleton
    fun characterDetailsStore(
        charactersDataSource: CharactersDataSource,
        dao: CharactersDao,
        @Named("character_details") lastRequests: LastRequests
    ): CharacterDetailsStore = StoreBuilder.from(
        fetcher = Fetcher.of { params: CharacterParams ->
            charactersDataSource(params).map { it.data.character }.fetcherDefaults(lastRequests, params)
        },
        sourceOfTruth = SourceOfTruth.of(
            reader = { params: CharacterParams ->
                dao.entryNullable(params.id.toString()).sourceReaderFilter(lastRequests, params)
            },
            writer = { params, response ->
                dao.withTransaction {
                    dao.updateOrInsert(response)
                }
            },
            delete = { dao.delete(it.id.toString()) },
            deleteAll = { dao.deleteAll() },
        )
    ).build()

    @Provides
    @Singleton
    @Named("character_details")
    fun characterDetailsLastRequests(preferences: PreferencesStore) = LastRequests("artist_details", preferences)
}
