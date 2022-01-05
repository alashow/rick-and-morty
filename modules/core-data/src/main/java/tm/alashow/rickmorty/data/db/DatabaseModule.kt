/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.rickmorty.data.db

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dagger.hilt.migration.DisableInstallInCheck
import tm.alashow.data.db.DatabaseTxRunner
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {
    @Singleton
    @Provides
    fun appDatabase(context: Context): AppDatabase {
        val builder = Room.databaseBuilder(context, AppDatabase::class.java, "app.db")
            .fallbackToDestructiveMigration()
        return builder.build()
    }

    @Singleton
    @Provides
    fun databaseTransactionRunner(db: AppDatabase): DatabaseTxRunner = DatabaseTxRunner(db)
}

@Module
@DisableInstallInCheck
object TestDatabaseModule {
    @Singleton
    @Provides
    fun provideTestDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }
}
