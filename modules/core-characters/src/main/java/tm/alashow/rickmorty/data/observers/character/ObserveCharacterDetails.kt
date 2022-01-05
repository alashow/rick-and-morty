/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.rickmorty.data.observers.character

import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import tm.alashow.data.SubjectInteractor
import tm.alashow.rickmorty.data.CharacterParams
import tm.alashow.rickmorty.data.db.daos.CharactersDao
import tm.alashow.rickmorty.data.interactors.character.GetCharacterDetails
import tm.alashow.rickmorty.domain.entities.Character

class ObserveCharacter @Inject constructor(
    private val charactersDao: CharactersDao,
) : SubjectInteractor<CharacterParams, Character>() {
    override fun createObservable(params: CharacterParams): Flow<Character> = charactersDao.entry(params.id.toString())
}

class ObserveCharacterDetails @Inject constructor(
    private val getCharacterDetails: GetCharacterDetails,
) : SubjectInteractor<GetCharacterDetails.Params, Character>() {
    override fun createObservable(params: GetCharacterDetails.Params) = getCharacterDetails(params)
}
