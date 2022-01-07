/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.rickmorty.data.observers.character

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import tm.alashow.data.SubjectInteractor
import tm.alashow.rickmorty.data.CharactersParams
import tm.alashow.rickmorty.data.db.daos.CharactersDao
import javax.inject.Inject

class ObserveCharactersFilterOptions @Inject constructor(
    private val dao: CharactersDao,
) : SubjectInteractor<Unit, CharactersParams.FilterOptions>() {

    override fun createObservable(params: Unit): Flow<CharactersParams.FilterOptions> = dao.entries().map { characters ->
        val statusOptions = characters.map { it.status }.toSet()
        val speciesOptions = characters.map { it.species }.toSet()
        val genderOptions = characters.map { it.gender }.toSet()
        val typeOptions = characters.map { it.type }.toSet()
        val originOptions = characters.map { it.origin.name }.toSet()
        val locationOptions = characters.map { it.location.name }.toSet()

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
