/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.rickmorty.data

import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import tm.alashow.base.di.TestAppModule
import tm.alashow.rickmorty.data.db.TestDatabaseModule

@Module(includes = [TestAppModule::class, TestDatabaseModule::class])
@InstallIn(SingletonComponent::class)
class TestModule
