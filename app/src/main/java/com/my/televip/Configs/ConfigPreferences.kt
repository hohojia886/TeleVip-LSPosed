package com.my.televip.Configs

import android.app.Activity
import android.content.SharedPreferences
import com.my.televip.application.ApplicationLoaderHook
import com.my.televip.logging.Logger

object ConfigPreferences {

    private lateinit var sharedPreferences: SharedPreferences

    @JvmStatic
    fun init() {
        sharedPreferences = ApplicationLoaderHook.getApplicationContext().getSharedPreferences("TeleVip", Activity.MODE_PRIVATE)
    }

    @JvmStatic
    fun getBoolean(key: String?): Boolean {
        return try {
            sharedPreferences.getBoolean(key, false)
        } catch (e: ClassCastException) {
            sharedPreferences.edit().remove(key).apply()
            false
        }
    }

    @JvmStatic
    fun putBoolean(key: String?, b: Boolean) {
        try {
            sharedPreferences.edit().putBoolean(key, b).apply()
        } catch (e: ClassCastException) {
            sharedPreferences.edit().remove(key).apply()
        }
    }

    @JvmStatic
    fun getInt(key: String?): Int {
        return try {
            sharedPreferences.getInt(key, 0)
        } catch (e: ClassCastException) {
            sharedPreferences.edit().remove(key).apply()
            0
        }
    }

    @JvmStatic
    fun putInt(key: String?, varValue: Int) {
        try {
            sharedPreferences.edit().putInt(key, varValue).apply()
        } catch (e: ClassCastException) {
            sharedPreferences.edit().remove(key).apply()
        }
    }

    @JvmStatic
    fun getString(key: String?): String? {
        return try {
            sharedPreferences.getString(key, null)
        } catch (e: ClassCastException) {
            sharedPreferences.edit().remove(key).apply()
            null
        }
    }

    @JvmStatic
    fun putString(key: String?, v: String?) {
        try {
            sharedPreferences.edit().putString(key, v).apply()
        } catch (e: ClassCastException) {
            sharedPreferences.edit().remove(key).apply()
        }
    }

    @JvmStatic
    fun remove(key: String?) {
        try {
            sharedPreferences.edit().remove(key).apply()
        } catch (t: Throwable) {
            Logger.e(t)
        }
    }
}
