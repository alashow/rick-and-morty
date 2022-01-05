/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.rickmorty.data.interactors.character

import javax.inject.Inject
import kotlinx.coroutines.withContext
import tm.alashow.base.util.CoroutineDispatchers
import tm.alashow.data.ResultInteractor
import tm.alashow.data.fetch
import tm.alashow.rickmorty.data.CharactersParams
import tm.alashow.rickmorty.data.repos.character.CharactersStore
import tm.alashow.rickmorty.domain.entities.Character

class GetCharacters @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val charactersStore: CharactersStore,
) : ResultInteractor<GetCharacters.Params, List<Character>>() {

    data class Params(val charactersParams: CharactersParams, val forceRefresh: Boolean = false)

    override suspend fun doWork(params: Params) = withContext(dispatchers.network) {
        charactersStore.fetch(params.charactersParams, params.forceRefresh)
    }
}
