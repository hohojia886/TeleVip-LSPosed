package com.my.televip.features.media

import com.my.televip.Class.ClassLoad
import com.my.televip.Class.ClassNames
import com.my.televip.Configs.ConfigManager
import com.my.televip.base.BaseMethodHook
import com.my.televip.hooks.HMethod
import com.my.televip.logging.Logger
import com.my.televip.obfuscate.Obfuscate

object EnableSavingStories {

    @JvmField
    var isEnable: Boolean = false

    @JvmStatic
    fun init() {
        try {
            if (!isEnable) {
                isEnable = true

                val holderClass = ClassLoad.getClass(ClassNames.STORY_ITEM_HOLDER)
                if (holderClass != null) {
                    HMethod.hookMethod(
                        holderClass,
                        Obfuscate.getMethodName("PeerStoriesView\$StoryItemHolder", "allowScreenshots"),
                        object : BaseMethodHook() {
                            override fun beforeMethod(param: MethodHookParam) {
                                if (ConfigManager.enableSavingStories?.isEnable == true) {
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
