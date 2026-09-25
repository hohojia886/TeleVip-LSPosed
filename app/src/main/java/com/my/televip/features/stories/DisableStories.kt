package com.my.televip.features.stories

import com.my.televip.clazz.ClassLoad
import com.my.televip.clazz.ClassNames
import com.my.televip.clients.ClientManager
import com.my.televip.configs.ConfigManager
import com.my.televip.base.BaseMethodHook
import com.my.televip.hooks.HMethod
import com.my.televip.logging.Logger
import com.my.televip.obfuscate.ArgsResolver
import com.my.televip.obfuscate.Obfuscate
import de.robv.android.xposed.XposedHelpers

object DisableStories {

    @JvmField
    var isEnable: Boolean = false

    @JvmStatic
    fun init() {
        try {
            if (!isEnable) {
                isEnable = true

                val mcClass = ClassLoad.getClass(ClassNames.MESSAGES_CONTROLLER)
                if (mcClass != null) {
                    HMethod.hookMethod(
                        mcClass,
                        "MessagesController",
                        arrayOf("storiesEnabled", "storyEntitiesAllowed"),
                        object : BaseMethodHook() {
                            override fun beforeMethod(param: MethodHookParam) {
                                if (ConfigManager.disableStories?.isEnable == true) param.result = false
                            }
                        }
                    )

                    val types: Array<Class<*>> = arrayOf(ClassLoad.getClass(ClassNames.TLRPC_USER)!!)
                    val merged = ArgsResolver.merge("storyEntitiesAllowed", types, object : BaseMethodHook() {
                        override fun beforeMethod(param: MethodHookParam) {
                            if (ConfigManager.disableStories?.isEnable == true) param.result = false
                        }
                    })
                    if (merged != null) {
                        XposedHelpers.findAndHookMethod(
                            mcClass,
                            Obfuscate.getMethodName("MessagesController", "storyEntitiesAllowed2"),
                            *merged
                        )
                    }
                }

                val scClass = ClassLoad.getClass(ClassNames.STORIES_CONTROLLER)
                if (scClass != null) {
                    if (ClientManager.`is`(ClientManager.Client.NagramX)) {
                        HMethod.hookMethod(
                            scClass,
                            "hasStories",
                            Long::class.javaPrimitiveType!!,
                            object : BaseMethodHook() {
                                override fun beforeMethod(param: MethodHookParam) {
                                    if (ConfigManager.disableStories?.isEnable == true) param.result = false
                                }
                            }
                        )
                    } else {
                        HMethod.hookMethod(
                            scClass,
                            Obfuscate.getMethodName("StoriesController", "hasStories"),
                            object : BaseMethodHook() {
                                override fun beforeMethod(param: MethodHookParam) {
                                    if (ConfigManager.disableStories?.isEnable == true) param.result = false
                                }
                            }
                        )
                    }
                }
            }
        } catch (t: Throwable) {
            Logger.e(t)
        }
    }
}
