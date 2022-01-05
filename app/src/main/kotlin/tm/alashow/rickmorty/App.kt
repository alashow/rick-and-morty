/*
 * Copyright (C) 2021, Alashov Berkeli
 * All rights reserved.
 */
package tm.alashow.rickmorty

import android.content.Context
import androidx.multidex.MultiDex
import dagger.hilt.android.HiltAndroidApp
import tm.alashow.base.BaseApp
import tm.alashow.base.inititializer.AppInitializers
import javax.inject.Inject

@HiltAndroidApp
class App : BaseApp() {

    @Inject
    lateinit var initializers: AppInitializers

    override fun onCreate() {
        super.onCreate()
        initializers.init(this)
    }

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
}
