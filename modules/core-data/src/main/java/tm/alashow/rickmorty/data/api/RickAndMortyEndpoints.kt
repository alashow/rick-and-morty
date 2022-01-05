/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.rickmorty.data.api

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap
import tm.alashow.rickmorty.domain.entities.CharacterId
import tm.alashow.rickmorty.domain.models.ApiResponse

interface RickAndMortyEndpoints {

    @JvmSuppressWildcards
    @GET("/characters/{id}")
    suspend fun character(@Path("id") id: CharacterId, @QueryMap params: Map<String, Any>): ApiResponse
}
