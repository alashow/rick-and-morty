/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.rickmorty.data.api

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class ApiModule {
    @Provides
    @Singleton
    fun provideEndpoints(retrofit: Retrofit): RickAndMortyEndpoints = retrofit.create(RickAndMortyEndpoints::class.java)
}
