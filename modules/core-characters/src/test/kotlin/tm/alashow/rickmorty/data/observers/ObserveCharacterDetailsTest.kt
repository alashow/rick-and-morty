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
import tm.alashow.rickmorty.data.observers.character.ObserveCharacterDetails

@UninstallModules(DatabaseModule::class)
@HiltAndroidTest
class ObserveCharacterDetailsTest : BaseTest() {

    @Inject lateinit var database: AppDatabase
    @Inject lateinit var observeCharacterDetails: ObserveCharacterDetails

    private val testParams = GetCharacterDetails.Params(-1)

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun `observing character details by id = 1 returns Rick Sanchez`() = runTest {
        val testId = 1L
        observeCharacterDetails(testParams.copy(characterId = testId))

        assertThat(observeCharacterDetails.get().name)
            .isEqualTo("Rick Sanchez")

        observeCharacterDetails.flow.test {
            val result = awaitItem()
            assertThat(result.name)
                .isEqualTo("Rick Sanchez")
            assertThat(result.origin.dimension)
                .contains("Dimension C-137")
            assertThat(result.location.residents)
                .isNotEmpty()
        }
    }
}
