package com.my.televip.features

import com.my.televip.Class.ClassLoad
import com.my.televip.Class.ClassNames
import com.my.televip.Configs.ConfigManager
import com.my.televip.base.AbstractMethodHook
import com.my.televip.hooks.HMethod
import com.my.televip.logging.Logger
import com.my.televip.obfuscate.AutomationResolver

object EnableSavingStories {
    @JvmField var isEnable: Boolean = false

    @JvmStatic
    fun init() {
        try {
            if (!isEnable) {
                isEnable = true
                val itemHolderClass = ClassLoad.getClass(ClassNames.STORY_ITEM_HOLDER)
                if (itemHolderClass != null) {
                    HMethod.hookMethod(
                        itemHolderClass,
                        AutomationResolver.resolve("PeerStoriesView\$StoryItemHolder", "allowScreenshots", AutomationResolver.ResolverType.Method),
                        object : AbstractMethodHook() {
                            override fun beforeMethod(param: MethodHookParam) {
                                if (ConfigManager.enableSavingStories.isEnable) {
                                    param.result = true
                                }
                            }
                        }
                    )
                }
            }
        } catch (t: Throwable) {
            Logger.e(t)
        }
    }
}
