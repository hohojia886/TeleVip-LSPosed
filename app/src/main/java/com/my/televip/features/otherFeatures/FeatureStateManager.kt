package com.my.televip.features.otherFeatures

import android.content.Context
import com.my.televip.Configs.ConfigPreferences

object FeatureStateManager {
    const val KEY_CHAT = "ChatOnItemClick"
    const val KEY_CHAT_BOOL = "ChatOnItemClick_boolean"

    const val KEY_PROFILE = "ProfileOnItemClick"
    const val KEY_PROFILE_BOOL = "ProfileOnItemClick_boolean"

    @JvmStatic
    val isChatEnabled: Boolean
        get() = ConfigPreferences.getBoolean(KEY_CHAT_BOOL)

    @JvmStatic
    val chatClass: String?
        get() = ConfigPreferences.getString(KEY_CHAT)

    @JvmStatic
    val isProfileEnabled: Boolean
        get() = ConfigPreferences.getBoolean(KEY_PROFILE_BOOL)

    @JvmStatic
    val profileClass: String?
        get() = ConfigPreferences.getString(KEY_PROFILE)

    @JvmStatic
    fun saveChat(className: String) {
        ConfigPreferences.putBoolean(KEY_CHAT_BOOL, true)
        ConfigPreferences.putString(KEY_CHAT, className)
    }

    @JvmStatic
    fun saveProfile(className: String) {
        ConfigPreferences.putBoolean(KEY_PROFILE_BOOL, true)
        ConfigPreferences.putString(KEY_PROFILE, className)
    }

    @JvmStatic
    fun reset(context: Context) {
        ConfigPreferences.remove(KEY_CHAT)
        ConfigPreferences.remove(KEY_CHAT_BOOL)
        ConfigPreferences.remove(KEY_PROFILE)
        ConfigPreferences.remove(KEY_PROFILE_BOOL)
        FeatureInitializer.init(context)
    }
}
