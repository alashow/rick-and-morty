/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.rickmorty.data.observers.character

import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import tm.alashow.data.SubjectInteractor
import tm.alashow.rickmorty.data.CharactersParams
import tm.alashow.rickmorty.data.db.daos.CharactersDao
import tm.alashow.rickmorty.domain.entities.Character

class ObserveCharactersFilterOptions @Inject constructor(
    private val dao: CharactersDao,
) : SubjectInteractor<Unit, CharactersParams.FilterOptions>() {

    private fun List<Character>.toOptions(selector: (Character) -> String) = map(selector).toSortedSet()

    override fun createObservable(params: Unit): Flow<CharactersParams.FilterOptions> = dao.entries().map { characters ->
        val statusOptions = characters.toOptions { it.status }
        val speciesOptions = characters.toOptions { it.species }
        val genderOptions = characters.toOptions { it.gender }
        val typeOptions = characters.toOptions { it.type }
        val originOptions = characters.toOptions { it.origin.name }
        val locationOptions = characters.toOptions { it.location.name }

        CharactersParams.FilterOptions(
            statusOptions = statusOptions,
            speciesOptions = speciesOptions,
            genderOptions = genderOptions,
            typeOptions = typeOptions,
            originOptions = originOptions,
            locationOptions = locationOptions
        )
    }
}
