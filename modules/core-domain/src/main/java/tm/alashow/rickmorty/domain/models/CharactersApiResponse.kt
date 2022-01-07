/*
 * Copyright (C) 2022, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.rickmorty.domain.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import tm.alashow.rickmorty.domain.entities.Character

@Serializable
data class CharactersApiResponse(
    @SerialName("info")
    val paginationInfo: PaginationInfo = PaginationInfo(),

    @SerialName("results")
    val results: List<Character> = emptyList(),
) : PaginatedApiResponse() {
    override val isSuccessful = true
}
