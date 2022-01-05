/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.rickmorty.data

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import tm.alashow.Config
import tm.alashow.base.di.TestAppModule
import tm.alashow.domain.models.DEFAULT_JSON_FORMAT
import tm.alashow.rickmorty.data.db.TestDatabaseModule

@Module(
    includes = [
        TestAppModule::class,
        TestDatabaseModule::class
    ]
)
@InstallIn(SingletonComponent::class)
class TestModule {

    @Provides
    fun provideOkhttpClient(): OkHttpClient = OkHttpClient.Builder().build()

    @OptIn(ExperimentalSerializationApi::class)
    @Provides
    fun provideRetrofit(client: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(Config.API_BASE_URL)
        .addConverterFactory(DEFAULT_JSON_FORMAT.asConverterFactory("application/json".toMediaType()))
        .client(client)
        .build()
}
