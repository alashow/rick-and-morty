/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.rickmorty.data.api

import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.QueryMap
import retrofit2.http.Url
import tm.alashow.rickmorty.domain.entities.Character
import tm.alashow.rickmorty.domain.entities.CharacterId
import tm.alashow.rickmorty.domain.entities.Location
import tm.alashow.rickmorty.domain.models.CharactersApiResponse

interface RickAndMortyEndpoints {

    @JvmSuppressWildcards
    @GET("/api/character/")
    suspend fun characters(@QueryMap params: Map<String, Any> = emptyMap()): CharactersApiResponse

    @JvmSuppressWildcards
    @GET("/api/character/{id}")
    suspend fun character(@Path("id") id: CharacterId): Character

    @JvmSuppressWildcards
    @GET
    suspend fun locationByUrl(@Url url: String): Location
}
