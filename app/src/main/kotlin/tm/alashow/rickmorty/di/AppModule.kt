/*
 * Copyright (C) 2018, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.rickmorty.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import tm.alashow.base.imageloading.CoilAppInitializer
import tm.alashow.base.inititializer.AppInitializers
import tm.alashow.base.inititializer.ThreeTenAbpInitializer
import tm.alashow.base.inititializer.TimberInitializer
import tm.alashow.base.util.CoroutineDispatchers

@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Singleton
    @Provides
    fun coroutineDispatchers() = CoroutineDispatchers(
        network = Dispatchers.IO,
        io = Dispatchers.IO,
        computation = Dispatchers.Default,
        main = Dispatchers.Main
    )

    @Provides
    fun appInitializers(
        timberManager: TimberInitializer,
        threeTen: ThreeTenAbpInitializer,
        coilAppInitializer: CoilAppInitializer,
    ): AppInitializers {
        return AppInitializers(
            timberManager,
            threeTen,
            coilAppInitializer,
        )
    }
}
