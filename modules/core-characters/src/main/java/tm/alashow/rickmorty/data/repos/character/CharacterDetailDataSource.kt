/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.rickmorty.data.repos.character

import javax.inject.Inject
import kotlinx.coroutines.flow.first
import tm.alashow.base.util.CoroutineDispatchers
import tm.alashow.data.resultApiCall
import tm.alashow.rickmorty.data.api.RickAndMortyEndpoints
import tm.alashow.rickmorty.data.db.daos.CharactersDao
import tm.alashow.rickmorty.domain.entities.Character
import tm.alashow.rickmorty.domain.entities.CharacterId

class CharacterDetailDataSource @Inject constructor(
    private val dao: CharactersDao,
    private val endpoints: RickAndMortyEndpoints,
    private val dispatchers: CoroutineDispatchers
) {
    suspend operator fun invoke(params: CharacterId): Result<Character> {
        return resultApiCall(dispatchers.network) {
            // get it from database or fallback to network
            val result = dao.entryNullable(params.toString()).first() ?: endpoints.character(params)
            val locationDetails = when (result.location.isUnknown) {
                true -> result.location
                else -> endpoints.locationByUrl(result.location.url)
            }
            val originDetails = when (result.origin.isUnknown) {
                true -> result.origin
                else -> endpoints.locationByUrl(result.origin.url)
            }

            result.copy(
                origin = originDetails,
                location = locationDetails
            )
        }
    }
}
