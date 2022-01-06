/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.rickmorty.data.observers.character

import javax.inject.Inject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import tm.alashow.data.SubjectInteractor
import tm.alashow.rickmorty.data.interactors.character.GetCharacterDetails
import tm.alashow.rickmorty.data.interactors.character.GetCharacterWithLocationResidents
import tm.alashow.rickmorty.domain.entities.Character

class ObserveCharacterDetails @Inject constructor(
    private val getCharacterDetails: GetCharacterDetails,
    private val getCharacterWithLocationResidents: GetCharacterWithLocationResidents
) : SubjectInteractor<GetCharacterDetails.Params, Character>() {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun createObservable(params: GetCharacterDetails.Params) = getCharacterDetails(params)
        .flatMapLatest {
            getCharacterWithLocationResidents(it)
            getCharacterWithLocationResidents.flow
        }
}
