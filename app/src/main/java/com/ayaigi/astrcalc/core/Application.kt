package com.ayaigi.astrcalc.core

import timber.log.Timber
import android.app.Application as App

class Application : App() {
    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())
    }
}