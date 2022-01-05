/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.rickmorty.data.observers

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import javax.inject.Inject
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Test
import tm.alashow.base.testing.BaseTest
import tm.alashow.rickmorty.data.db.AppDatabase
import tm.alashow.rickmorty.data.db.DatabaseModule
import tm.alashow.rickmorty.data.interactors.character.GetCharacterDetails
import tm.alashow.rickmorty.data.observers.character.ObserveCharacter

@UninstallModules(DatabaseModule::class)
@HiltAndroidTest
class ObserveCharacterTest : BaseTest() {

    @Inject lateinit var database: AppDatabase
    @Inject lateinit var getCharactersDetails: GetCharacterDetails
    @Inject lateinit var observeCharacter: ObserveCharacter

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun `observing character by id = 1 returns null if it's not in database yet`() = runTest {
        observeCharacter(1)

        observeCharacter.flow.test {
            assertThat(awaitItem())
                .isEqualTo(null)
        }
    }

    @Test
    fun `observing character by id = 1 returns Rick Sanchez if it's in database`() = runTest {
        val testId = 1L
        getCharactersDetails.execute(GetCharacterDetails.Params(characterId = testId))

        observeCharacter(testId)
        observeCharacter.flow.test {
            assertThat(awaitItem()?.name)
                .isEqualTo("Rick Sanchez")
        }
    }
}
