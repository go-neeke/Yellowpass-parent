package kr.co.yellowpass.parent

import android.app.Application
import timber.log.Timber

class YellowpassApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        Prefs.init(this) // 🔥 여기서 초기화
        Timber.plant(Timber.DebugTree())
    }
}