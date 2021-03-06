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
import tm.alashow.rickmorty.data.repos.character.CharacterDetailsStore
import tm.alashow.rickmorty.domain.entities.Character
import tm.alashow.rickmorty.domain.entities.CharacterId

class GetCharacterDetails @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val characterDetailsStore: CharacterDetailsStore,
) : ResultInteractor<GetCharacterDetails.Params, Character>() {

    data class Params(val characterId: CharacterId, val forceRefresh: Boolean = false)

    override suspend fun doWork(params: Params) = withContext(dispatchers.network) {
        characterDetailsStore.fetch(params.characterId, params.forceRefresh)
    }
}
