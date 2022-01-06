/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.rickmorty.data.interactors.character

import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import tm.alashow.data.SubjectInteractor
import tm.alashow.rickmorty.domain.entities.Character
import tm.alashow.rickmorty.domain.entities.Location

class GetCharacterWithLocationResidents @Inject constructor(
    private val getCharactersByUrls: GetCharactersByUrls,
) : SubjectInteractor<Character, Character>() {

    private fun Location.applyResidents(charactersByUrl: Map<String, Character>) = copy(
        residentsCharacters = residents.map { r ->
            val id = r.split("/").last().toLong()
            charactersByUrl[r] ?: Character.createUnknown(id)
        }
    )

    override fun createObservable(params: Character): Flow<Character> {
        val origin = params.origin
        val location = params.location
        return if (!origin.isUnknown || !location.isUnknown) {
            getCharactersByUrls(GetCharactersByUrls.Params(origin.residents + location.residents))
            getCharactersByUrls.flow.map {
                val charactersByUrl = it.associateBy { c -> c.url }
                params.copy(
                    origin = origin.applyResidents(charactersByUrl),
                    location = location.applyResidents(charactersByUrl)
                )
            }
        } else flowOf(params)
    }
}
