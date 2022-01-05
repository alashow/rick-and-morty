/*
 * Copyright (C) 2018, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.base.di

import android.app.Application
import android.content.Context
import android.content.res.Resources
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object BaseModule {

    @Provides
    fun appContext(app: Application): Context = app.applicationContext

    @Provides
    fun appResources(app: Application): Resources = app.resources
}
