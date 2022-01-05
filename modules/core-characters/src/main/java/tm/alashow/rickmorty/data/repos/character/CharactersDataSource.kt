/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.rickmorty.data.repos.character

import javax.inject.Inject
import tm.alashow.base.util.CoroutineDispatchers
import tm.alashow.data.resultApiCall
import tm.alashow.rickmorty.data.CharactersParams
import tm.alashow.rickmorty.data.CharactersParams.Companion.toQueryMap
import tm.alashow.rickmorty.data.api.RickAndMortyEndpoints
import tm.alashow.rickmorty.domain.models.CharactersApiResponse
import tm.alashow.rickmorty.domain.models.checkForErrors

class CharactersDataSource @Inject constructor(
    private val endpoints: RickAndMortyEndpoints,
    private val dispatchers: CoroutineDispatchers
) {
    suspend operator fun invoke(params: CharactersParams): Result<CharactersApiResponse> {
        return resultApiCall(dispatchers.network) {
            endpoints.characters(params.toQueryMap())
                .checkForErrors()
        }
    }
}
