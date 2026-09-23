package com.my.televip.features.otherFeatures

import com.my.televip.Configs.ConfigPreferences

object FeatureStateManager {

    const val KEY_CHAT = "ChatOnItemClick"
    const val KEY_CHAT_BOOL = "ChatOnItemClick_boolean"

    const val KEY_PROFILE = "ProfileOnItemClick"
    const val KEY_PROFILE_BOOL = "ProfileOnItemClick_boolean"

    @JvmStatic
    fun isChatEnabled(): Boolean {
        return ConfigPreferences.getBoolean(KEY_CHAT_BOOL)
    }

    @JvmStatic
    fun getChatClass(): String? {
        return ConfigPreferences.getString(KEY_CHAT)
    }

    @JvmStatic
    fun isProfileEnabled(): Boolean {
        return ConfigPreferences.getBoolean(KEY_PROFILE_BOOL)
    }

    @JvmStatic
    fun getProfileClass(): String? {
        return ConfigPreferences.getString(KEY_PROFILE)
    }

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
    fun reset() {
        ConfigPreferences.remove(KEY_CHAT)
        ConfigPreferences.remove(KEY_CHAT_BOOL)
        ConfigPreferences.remove(KEY_PROFILE)
        ConfigPreferences.remove(KEY_PROFILE_BOOL)
        FeatureInitializer.init()
    }
}
