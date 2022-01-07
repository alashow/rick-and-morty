/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.rickmorty.data.repos.character

import javax.inject.Inject
import retrofit2.HttpException
import tm.alashow.base.util.CoroutineDispatchers
import tm.alashow.data.resultApiCall
import tm.alashow.rickmorty.data.CharactersParams
import tm.alashow.rickmorty.data.CharactersParams.Companion.toQueryMap
import tm.alashow.rickmorty.data.api.RickAndMortyEndpoints
import tm.alashow.rickmorty.domain.models.CharactersApiResponse
import tm.alashow.rickmorty.domain.models.checkForErrors
import tm.alashow.rickmorty.domain.models.errors.EmptyResultException

class CharactersDataSource @Inject constructor(
    private val endpoints: RickAndMortyEndpoints,
    private val dispatchers: CoroutineDispatchers
) {
    suspend operator fun invoke(params: CharactersParams): Result<CharactersApiResponse> {
        return resultApiCall(dispatchers.network) {
            try {
                endpoints.characters(params.toQueryMap())
                    .checkForErrors()
            } catch (e: HttpException) {
                // api returns 404 if page is out of range
                if (e.code() == 404) throw EmptyResultException()
                else throw e
            }
        }
    }
}
