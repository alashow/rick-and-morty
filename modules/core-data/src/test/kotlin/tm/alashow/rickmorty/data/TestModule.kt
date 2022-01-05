/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.rickmorty.data

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import tm.alashow.rickmorty.data.db.TestDatabaseModule

@Module(includes = [TestDatabaseModule::class])
@InstallIn(SingletonComponent::class)
object TestModule
