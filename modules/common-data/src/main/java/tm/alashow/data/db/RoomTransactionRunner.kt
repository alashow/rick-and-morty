/*
 * Copyright (C) 2018, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.data.db

import androidx.room.RoomDatabase
import androidx.room.withTransaction

interface DatabaseTransactionRunner {
    suspend operator fun <T> invoke(block: suspend () -> T): T
}

class RoomTransactionRunner(private val db: RoomDatabase) : DatabaseTransactionRunner {
    override suspend operator fun <T> invoke(block: suspend () -> T): T {
        return db.withTransaction {
            block()
        }
    }
}

class TestTransactionRunner : DatabaseTransactionRunner {
    override suspend fun <T> invoke(block: suspend () -> T): T = block()
}
