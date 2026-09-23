package com.my.televip.settings

import com.my.televip.Configs.ConfigPreferences
import com.my.televip.language.Keys
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object SettingsRepository {

    private val _hideSeenPrivateChat = MutableStateFlow(ConfigPreferences.getBoolean(Keys.HideSeenPrivateChat))
    val hideSeenPrivateChat: StateFlow<Boolean> = _hideSeenPrivateChat.asStateFlow()

    private val _hideSeenChannel = MutableStateFlow(ConfigPreferences.getBoolean(Keys.HideSeenChannel))
    val hideSeenChannel: StateFlow<Boolean> = _hideSeenChannel.asStateFlow()

    private val _hideTyping = MutableStateFlow(ConfigPreferences.getBoolean(Keys.HideTyping))
    val hideTyping: StateFlow<Boolean> = _hideTyping.asStateFlow()

    private val _hideOnline = MutableStateFlow(ConfigPreferences.getBoolean(Keys.HideOnline))
    val hideOnline: StateFlow<Boolean> = _hideOnline.asStateFlow()

    private val _hidePhone = MutableStateFlow(ConfigPreferences.getBoolean(Keys.HidePhone))
    val hidePhone: StateFlow<Boolean> = _hidePhone.asStateFlow()

    fun updateSetting(key: String, value: Boolean) {
        ConfigPreferences.putBoolean(key, value)
        when (key) {
            Keys.HideSeenPrivateChat -> _hideSeenPrivateChat.value = value
            Keys.HideSeenChannel -> _hideSeenChannel.value = value
            Keys.HideTyping -> _hideTyping.value = value
            Keys.HideOnline -> _hideOnline.value = value
            Keys.HidePhone -> _hidePhone.value = value
        }
    }
}
