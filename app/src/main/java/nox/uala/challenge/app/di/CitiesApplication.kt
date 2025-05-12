package nox.uala.challenge.app.di

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import nox.uala.challenge.core.util.AppLogger

@HiltAndroidApp
class CitiesApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AppLogger.init()
    }
}