package com.my.televip.features.base

import android.content.Context
import com.my.televip.Configs.ConfigItem

interface Feature {
    fun getKey(): String
    fun isEnabled(): Boolean
    fun init(context: Context)
    fun createConfigItem(context: Context): ConfigItem?
}
