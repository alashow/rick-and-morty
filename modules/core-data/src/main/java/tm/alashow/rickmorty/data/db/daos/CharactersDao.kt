/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.rickmorty.data.db.daos

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import kotlinx.coroutines.flow.Flow
import tm.alashow.data.db.PaginatedEntryDao
import tm.alashow.rickmorty.data.CharactersParams
import tm.alashow.rickmorty.domain.entities.Character

@Dao
abstract class CharactersDao : PaginatedEntryDao<CharactersParams, Character>() {

    @Transaction
    @Query(
        """SELECT * FROM characters 
        WHERE (status LIKE (:status) 
                AND species LIKE (:species)
                AND type LIKE (:type)
                AND gender LIKE (:gender)
                AND origin LIKE (:origin)
                AND location LIKE (:location))
        AND params = :params
        ORDER BY page ASC, search_index ASC"""
    )
    abstract fun entriesPagingSourceWithFilters(
        status: String,
        species: String,
        type: String,
        gender: String,
        origin: String,
        location: String,
        params: CharactersParams
    ): PagingSource<Int, Character>

    @Transaction
    @Query("SELECT * FROM characters WHERE url IN (:urls) GROUP BY url  ORDER BY page ASC, search_index ASC")
    abstract fun getCharactersByUrls(urls: List<String>): Flow<List<Character>>

    @Transaction
    @Query("DELETE FROM characters WHERE name NOT IN (:names)")
    abstract suspend fun deleteExcept(names: Set<String>): Int

    @Transaction
    @Query("SELECT * FROM characters ORDER BY page ASC, search_index ASC")
    abstract override fun entries(): Flow<List<Character>>

    @Query("SELECT * FROM characters WHERE params = :params ORDER BY page ASC, search_index ASC")
    abstract override fun entries(params: CharactersParams): Flow<List<Character>>

    @Transaction
    @Query("SELECT * FROM characters WHERE params = :params and page = :page ORDER BY page ASC, search_index ASC")
    abstract override fun entries(params: CharactersParams, page: Int): Flow<List<Character>>

    @Transaction
    @Query("SELECT * FROM characters ORDER BY page ASC, search_index ASC LIMIT :count OFFSET :offset")
    abstract override fun entries(count: Int, offset: Int): Flow<List<Character>>

    @Transaction
    @Query("SELECT * FROM characters ORDER BY page ASC, search_index ASC")
    abstract override fun entriesPagingSource(): PagingSource<Int, Character>

    @Transaction
    @Query("SELECT * FROM characters WHERE params = :params ORDER BY page ASC, search_index ASC")
    abstract override fun entriesPagingSource(params: CharactersParams): PagingSource<Int, Character>

    @Transaction
    @Query("SELECT * FROM characters WHERE id = :id ORDER BY details_fetched DESC")
    abstract override fun entry(id: String): Flow<Character>

    @Transaction
    @Query("SELECT * FROM characters WHERE id in (:ids)")
    abstract override fun entriesById(ids: List<String>): Flow<List<Character>>

    @Transaction
    @Query("SELECT * FROM characters WHERE id = :id ORDER BY details_fetched DESC")
    abstract override fun entryNullable(id: String): Flow<Character?>

    @Query("DELETE FROM characters WHERE id = :id")
    abstract override suspend fun delete(id: String): Int

    @Query("DELETE FROM characters WHERE params = :params")
    abstract override suspend fun delete(params: CharactersParams): Int

    @Query("DELETE FROM characters WHERE params = :params and page = :page")
    abstract override suspend fun delete(params: CharactersParams, page: Int): Int

    @Query("DELETE FROM characters")
    abstract override suspend fun deleteAll(): Int

    @Query("SELECT COUNT(*) from characters")
    abstract override suspend fun count(): Int

    @Query("SELECT COUNT(*) from characters")
    abstract override fun observeCount(): Flow<Int>

    @Query("SELECT COUNT(*) from characters where params = :params")
    abstract override suspend fun count(params: CharactersParams): Int

    @Query("SELECT COUNT(*) from characters where id = :id")
    abstract override fun has(id: String): Flow<Int>

    @Query("SELECT COUNT(*) from characters where id = :id")
    abstract override suspend fun exists(id: String): Int
}
