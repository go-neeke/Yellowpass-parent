package kr.co.yellowpass.parent

import android.app.Application
import timber.log.Timber

class YellowpassApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}