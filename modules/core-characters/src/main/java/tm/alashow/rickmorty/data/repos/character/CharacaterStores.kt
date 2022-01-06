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
import javax.inject.Named
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.threeten.bp.Duration
import tm.alashow.data.LastRequests
import tm.alashow.data.PreferencesStore
import tm.alashow.data.db.DatabaseTransactionRunner
import tm.alashow.rickmorty.data.CharactersParams
import tm.alashow.rickmorty.data.db.daos.CharactersDao
import tm.alashow.rickmorty.domain.entities.Character
import tm.alashow.rickmorty.domain.entities.CharacterId
import tm.alashow.rickmorty.domain.models.errors.requireNonEmpty

typealias CharactersStore = Store<CharactersParams, List<Character>>
typealias CharacterDetailsStore = Store<CharacterId, Character>

@InstallIn(SingletonComponent::class)
@Module
object CharacterStoresModule {

    private suspend fun <T> Result<List<T>>.fetcherDefaults(lastRequests: LastRequests, params: CharactersParams) = onSuccess {
        lastRequests.save(params = params.toString() + params.page)
    }.requireNonEmpty()

    private fun <T> Flow<List<T>>.sourceReaderFilter(lastRequests: LastRequests, params: CharactersParams) = map { entries ->
        when {
            entries.isEmpty() -> null // because Store only treats nulls as no-value
            lastRequests.isExpired(params = params.toString() + params.page) -> null // this source is invalid if it's expired
            else -> entries
        }
    }

    private suspend fun <T> Result<T>.fetcherDefaults(lastRequests: LastRequests, params: CharacterId) = onSuccess {
        lastRequests.save(params.toString())
    }.getOrThrow()

    private fun <T> Flow<T?>.sourceReaderFilter(lastRequests: LastRequests, params: CharacterId) = map { entry ->
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
    fun charactersStore(
        dataSource: CharactersDataSource,
        dao: CharactersDao,
        txRunner: DatabaseTransactionRunner,
        @Named("characters") lastRequests: LastRequests
    ): CharactersStore = StoreBuilder.from(
        fetcher = Fetcher.of { params: CharactersParams ->
            dataSource(params).map { it.results }
                .fetcherDefaults(lastRequests, params)
        },
        sourceOfTruth = SourceOfTruth.of(
            reader = { params: CharactersParams ->
                dao.entries(params, params.page)
                    .sourceReaderFilter(lastRequests, params)
            },
            writer = { params, response ->
                txRunner {
                    // requesting the first page means we're refreshing the whole table
                    if (params.page == 0)
                        dao.deleteAll()
                    val entries = response.mapIndexed { index, it ->
                        it.copy(
                            params = params.toString(),
                            page = params.page,
                            searchIndex = index,
                            primaryKey = "${it.id}_${params.toString().hashCode()}",
                        )
                    }
                    dao.update(params, params.page, entries)
                }
            },
            delete = { dao.delete(params = it, page = it.page) },
            deleteAll = dao::deleteAll
        )
    ).build()

    @Provides
    @Singleton
    fun characterDetailsStore(
        charactersDataSource: CharacterDetailDataSource,
        dao: CharactersDao,
        txRunner: DatabaseTransactionRunner,
        @Named("character_details") lastRequests: LastRequests
    ): CharacterDetailsStore = StoreBuilder.from(
        fetcher = Fetcher.of { params: CharacterId ->
            charactersDataSource(params).fetcherDefaults(lastRequests, params)
        },
        sourceOfTruth = SourceOfTruth.of(
            reader = { params: CharacterId ->
                dao.entryNullable(params.toString()).sourceReaderFilter(lastRequests, params)
            },
            writer = { params, response ->
                txRunner {
                    dao.updateOrInsert(
                        response.copy(
                            params = params.toString(),
                            primaryKey = params.toString(),
                            detailsFetched = true,
                        )
                    )
                }
            },
            delete = { dao.delete(it.toString()) },
            deleteAll = dao::deleteAll,
        )
    ).build()

    @Provides
    @Singleton
    @Named("characters")
    fun charactersLastRequests(preferences: PreferencesStore) = LastRequests("characters", preferences, Duration.ofDays(7))

    @Provides
    @Singleton
    @Named("character_details")
    fun characterDetailsLastRequests(preferences: PreferencesStore) = LastRequests("artist_details", preferences)
}
