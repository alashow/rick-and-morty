/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.rickmorty.data.observers.character

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import tm.alashow.data.PaginatedEntryRemoteMediator
import tm.alashow.data.PagingInteractor
import tm.alashow.rickmorty.data.CharactersParams
import tm.alashow.rickmorty.data.db.daos.CharactersDao
import tm.alashow.rickmorty.data.interactors.character.GetCharacters
import tm.alashow.rickmorty.data.interactors.character.GetCharactersPagingSourceWithFilters
import tm.alashow.rickmorty.domain.entities.Character

@OptIn(ExperimentalPagingApi::class)
class ObservePagedCharacters @Inject constructor(
    private val getCharacters: GetCharacters,
    private val dao: CharactersDao,
    private val getCharactersPagingSourceWithFilters: GetCharactersPagingSourceWithFilters,
) : PagingInteractor<ObservePagedCharacters.Params, Character>() {

    override fun createObservable(
        params: Params
    ): Flow<PagingData<Character>> {
        return Pager(
            config = params.pagingConfig,
            remoteMediator = PaginatedEntryRemoteMediator { page, refreshing ->
                try {
                    getCharacters.execute(
                        GetCharacters.Params(
                            charactersParams = params.charactersParams.copy(page = page),
                            forceRefresh = refreshing
                        )
                    )
                } catch (error: Exception) {
                    onError(error)
                    throw error
                }
            },
            pagingSourceFactory = { getCharactersPagingSourceWithFilters(params.charactersParams) }
        ).flow
    }

    data class Params(
        val charactersParams: CharactersParams,
        override val pagingConfig: PagingConfig = DEFAULT_PAGING_CONFIG,
    ) : Parameters<Character>
}
