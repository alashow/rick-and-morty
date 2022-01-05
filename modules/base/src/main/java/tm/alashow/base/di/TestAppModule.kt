/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.base.di

import dagger.Module
import dagger.Provides
import dagger.hilt.migration.DisableInstallInCheck
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import tm.alashow.base.util.CoroutineDispatchers

@Module
@DisableInstallInCheck
class TestAppModule {

    @Singleton
    @Provides
    fun coroutineDispatchers() = CoroutineDispatchers(
        network = Dispatchers.Unconfined,
        io = Dispatchers.Unconfined,
        computation = Dispatchers.Unconfined,
        main = Dispatchers.Unconfined
    )
}
