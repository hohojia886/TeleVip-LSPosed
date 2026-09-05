package com.my.televip.features.base

import android.content.Context
import com.my.televip.Configs.ConfigItem
import com.my.televip.logging.Logger
import java.util.Collections

object FeatureRegistry {
    private val registeredFeatures = ArrayList<Feature>()

    @JvmStatic
    fun register(feature: Feature?) {
        if (feature != null && !registeredFeatures.contains(feature)) {
            registeredFeatures.add(feature)
        }
    }

    @JvmStatic
    fun getRegisteredFeatures(): List<Feature> {
        return Collections.unmodifiableList(registeredFeatures)
    }

    @JvmStatic
    fun initAll(context: Context) {
        for (feature in registeredFeatures) {
            try {
                if (feature.isEnabled()) {
                    feature.init(context)
                }
            } catch (t: Throwable) {
                Logger.e(t)
            }
        }
    }

    @JvmStatic
    fun getConfigItems(context: Context): List<ConfigItem> {
        val items = ArrayList<ConfigItem>()
        for (feature in registeredFeatures) {
            try {
                val item = feature.createConfigItem(context)
                if (item != null) {
                    items.add(item)
                }
            } catch (t: Throwable) {
                Logger.e(t)
            }
        }
        return items
    }
}
