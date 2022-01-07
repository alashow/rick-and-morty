/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.rickmorty.data.interactors.character

import androidx.paging.PagingSource
import javax.inject.Inject
import tm.alashow.rickmorty.data.CharactersParams
import tm.alashow.rickmorty.data.db.daos.CharactersDao
import tm.alashow.rickmorty.domain.entities.Character

class GetCharactersPagingSourceWithFilters @Inject constructor(
    private val dao: CharactersDao,
) {
    operator fun invoke(params: CharactersParams): PagingSource<Int, Character> = when (params.filters.hasFilters) {
        true -> dao.entriesPagingSourceWithFilters(
            // null == %% == any item
            status = params.filters.status ?: "%%",
            species = params.filters.species ?: "%%",
            type = params.filters.type ?: "%%",
            gender = params.filters.gender ?: "%%",
            origin = "%${params.filters.origin ?: ""}%",
            location = "%${params.filters.location ?: ""}%",
            params = params
        )
        else -> dao.entriesPagingSource(params)
    }
}
