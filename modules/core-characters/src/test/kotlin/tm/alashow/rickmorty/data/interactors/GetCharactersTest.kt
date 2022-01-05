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
import tm.alashow.base.testing.BaseTest
import tm.alashow.rickmorty.data.CharactersParams
import tm.alashow.rickmorty.data.db.AppDatabase
import tm.alashow.rickmorty.data.db.DatabaseModule
import tm.alashow.rickmorty.data.interactors.character.GetCharacters

@UninstallModules(DatabaseModule::class)
@HiltAndroidTest
class GetCharactersTest : BaseTest() {

    @Inject lateinit var database: AppDatabase
    @Inject lateinit var getCharacters: GetCharacters

    private val testParams = GetCharacters.Params(CharactersParams())

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun `get characters returns list of characters`() = runTest {
        val result = getCharacters.execute(testParams)

        assertThat(result).isNotEmpty()
    }

    @Test
    fun `get characters returns different list of characters given page`() = runTest {
        val result = getCharacters.execute(testParams)
        val secondPageResult = getCharacters.execute(testParams.copy(testParams.charactersParams.copy(page = 1)))
        val thirdPageResult = getCharacters.execute(testParams.copy(testParams.charactersParams.copy(page = 2)))

        assertThat(result).isNotEmpty()
        assertThat(secondPageResult).isNotEmpty()
        assertThat(thirdPageResult).isNotEmpty()

        assertThat(result).isNotEqualTo(secondPageResult)
        assertThat(result).isNotEqualTo(thirdPageResult)
        assertThat(secondPageResult).isNotEqualTo(thirdPageResult)
    }

    @Test
    fun `get characters returns list of filtered characters given name`() = runTest {
        listOf("Rick", "Morty", "Sanchez").forEach { name ->
            val result = getCharacters.execute(testParams.copy(testParams.charactersParams.copy(name = name)))

            assertThat(result).isNotEmpty()
            result.map { it.name.lowercase() }.forEach {
                assertThat(it).contains(name.lowercase())
            }
        }
    }

    @Test
    fun `get characters returns list of filtered characters given status`() = runTest {
        listOf("alive", "dead", "unknown").forEach { status ->
            val result = getCharacters.execute(testParams.copy(testParams.charactersParams.copy(status = status)))

            assertThat(result).isNotEmpty()
            assertThat(result.map { it.status.lowercase() }.toSet())
                .containsExactly(status)
        }
    }

    @Test
    fun `get characters returns list of filtered characters given species`() = runTest {
        listOf("human", "humanoid", "alien").forEach { species ->
            val result = getCharacters.execute(testParams.copy(testParams.charactersParams.copy(species = species)))

            assertThat(result).isNotEmpty()
            result.map { it.species.lowercase() }.forEach {
                assertThat(it).startsWith(species)
            }
        }
    }

    @Test
    fun `get characters returns list of filtered characters given type`() = runTest {
        listOf("bird-person", "parasite", "self-aware arm").forEach { type ->
            val result = getCharacters.execute(testParams.copy(testParams.charactersParams.copy(type = type)))

            assertThat(result).isNotEmpty()
            assertThat(result.map { it.type.lowercase() }.toSet())
                .contains(type)
        }
    }

    @Test
    fun `get characters returns list of filtered characters given gender`() = runTest {
        listOf("male", "female", "unknown").forEach { gender ->
            val result = getCharacters.execute(testParams.copy(testParams.charactersParams.copy(gender = gender)))

            assertThat(result).isNotEmpty()
            assertThat(result.map { it.gender.lowercase() }.toSet())
                .containsExactly(gender)
        }
    }
}
