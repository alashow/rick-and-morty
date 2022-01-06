/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.rickmorty.data.interactors.character

import javax.inject.Inject
import tm.alashow.base.util.CoroutineDispatchers
import tm.alashow.data.SubjectInteractor
import tm.alashow.rickmorty.data.db.daos.CharactersDao
import tm.alashow.rickmorty.domain.entities.Character

class GetCharactersByUrls @Inject constructor(
    private val dispatchers: CoroutineDispatchers,
    private val dao: CharactersDao,
) : SubjectInteractor<GetCharactersByUrls.Params, List<Character>>() {

    data class Params(val urls: List<String>)

    override fun createObservable(params: Params) = dao.getCharactersByUrls(params.urls)
}
