package kr.co.yellowpass.parent

import android.content.Context
import android.content.SharedPreferences

object Prefs {

    private lateinit var prefs: SharedPreferences

    fun init(context: Context) {
        prefs = context.getSharedPreferences("yellowpass_prefs", Context.MODE_PRIVATE)
    }

    var parentId: Long
        get() = prefs.getLong("parentId", -1)
        set(value) = prefs.edit().putLong("parentId", value).apply()

    var token: String?
        get() = prefs.getString("token", "")
        set(value) = prefs.edit().putString("token", value).apply()
}