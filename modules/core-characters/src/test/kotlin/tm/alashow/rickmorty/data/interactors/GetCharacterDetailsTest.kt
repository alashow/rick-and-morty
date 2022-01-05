/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.rickmorty.data.interactors

import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import javax.inject.Inject
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Test
import retrofit2.HttpException
import tm.alashow.base.testing.BaseTest
import tm.alashow.rickmorty.data.db.AppDatabase
import tm.alashow.rickmorty.data.db.DatabaseModule
import tm.alashow.rickmorty.data.interactors.character.GetCharacterDetails

@UninstallModules(DatabaseModule::class)
@HiltAndroidTest
class GetCharacterDetailsTest : BaseTest() {

    @Inject lateinit var database: AppDatabase
    @Inject lateinit var getCharacterDetails: GetCharacterDetails

    private val testParams = GetCharacterDetails.Params(-1)

    @After
    fun tearDown() {
        database.close()
    }

    @Test(expected = HttpException::class)
    fun `get character details given wrong id throws NotFound HttpException`() = runTest {
        // assuming default test params has wrong id
        getCharacterDetails.execute(testParams)
    }

    @Test
    fun `get character details by id = 1 returns Rick Sanchez character`() = runTest {
        val result = getCharacterDetails.execute(testParams.copy(characterId = 1))

        assertThat(result.name)
            .contains("Rick Sanchez")
    }

    @Test
    fun `get character details by id = 2 return Morty Smith character`() = runTest {
        val result = getCharacterDetails.execute(testParams.copy(characterId = 2))

        assertThat(result.name)
            .contains("Morty Smith")
    }
}
