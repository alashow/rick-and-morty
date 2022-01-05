/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.rickmorty.data.repos.character

import javax.inject.Inject
import tm.alashow.base.util.CoroutineDispatchers
import tm.alashow.data.resultApiCall
import tm.alashow.rickmorty.data.CharacterParams
import tm.alashow.rickmorty.data.CharacterParams.Companion.toQueryMap
import tm.alashow.rickmorty.data.api.RickAndMortyEndpoints
import tm.alashow.rickmorty.domain.models.ApiResponse
import tm.alashow.rickmorty.domain.models.checkForErrors

class CharactersDataSource @Inject constructor(
    private val endpoints: RickAndMortyEndpoints,
    private val dispatchers: CoroutineDispatchers
) {
    suspend operator fun invoke(params: CharacterParams): Result<ApiResponse> {
        return resultApiCall(dispatchers.network) {
            endpoints.character(params.id, params.toQueryMap())
                .checkForErrors()
        }
    }
}
