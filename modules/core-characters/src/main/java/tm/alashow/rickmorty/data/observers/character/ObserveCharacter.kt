/*
 * Copyright (C) 2022, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.rickmorty.data.observers.character

import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import tm.alashow.data.SubjectInteractor
import tm.alashow.rickmorty.data.db.daos.CharactersDao
import tm.alashow.rickmorty.domain.entities.Character
import tm.alashow.rickmorty.domain.entities.CharacterId

class ObserveCharacter @Inject constructor(
    private val charactersDao: CharactersDao,
) : SubjectInteractor<CharacterId, Character?>() {
    override fun createObservable(params: CharacterId): Flow<Character?> = charactersDao.entry(params.toString())
}
