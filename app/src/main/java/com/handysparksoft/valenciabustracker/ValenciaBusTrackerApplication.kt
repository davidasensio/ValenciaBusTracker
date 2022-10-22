package com.handysparksoft.valenciabustracker

import android.app.Application
import com.google.android.gms.ads.MobileAds
import timber.log.Timber

class ValenciaBusTrackerApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
            Timber.d("Timber logger initialized")
        }

        MobileAds.initialize(this)
    }
}
