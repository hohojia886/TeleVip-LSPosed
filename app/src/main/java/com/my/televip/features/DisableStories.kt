package com.my.televip.features

import com.my.televip.Class.ClassLoad
import com.my.televip.Class.ClassNames
import com.my.televip.ClientChecker
import com.my.televip.Configs.ConfigManager
import com.my.televip.base.AbstractMethodHook
import com.my.televip.hooks.HMethod
import com.my.televip.logging.Logger
import com.my.televip.obfuscate.AutomationResolver

object DisableStories {

    @JvmField
    var isEnable: Boolean = false

    @JvmStatic
    fun init() {
        try {
            if (!isEnable) {
                isEnable = true

                val messagesControllerClass = ClassLoad.getClass(ClassNames.MESSAGES_CONTROLLER)
                if (messagesControllerClass != null) {
                    HMethod.hookMethod(
                        messagesControllerClass,
                        "MessagesController",
                        arrayOf("storiesEnabled", "storyEntitiesAllowed"),
                        object : AbstractMethodHook() {
                            override fun beforeMethod(param: MethodHookParam) {
                                if (ConfigManager.disableStories.isEnable) {
                                    param.result = false
                                }
                            }
                        }
                    )

                    HMethod.hookMethod(
                        messagesControllerClass,
                        AutomationResolver.resolve("MessagesController", "storyEntitiesAllowed2", AutomationResolver.ResolverType.Method),
                        *AutomationResolver.merge(
                            AutomationResolver.resolveObject("storyEntitiesAllowed", arrayOf(ClassLoad.getClass(ClassNames.TLRPC_USER))),
                            object : AbstractMethodHook() {
                                override fun beforeMethod(param: MethodHookParam) {
                                    if (ConfigManager.disableStories.isEnable) {
                                        param.result = false
                                    }
                                }
                            }
                        )
                    )
                }

                val storiesControllerClass = ClassLoad.getClass(ClassNames.STORIES_CONTROLLER)
                if (storiesControllerClass != null) {
                    if (ClientChecker.check(ClientChecker.ClientType.NagramX)) {
                        HMethod.hookMethod(
                            storiesControllerClass,
                            AutomationResolver.resolve("StoriesController", "hasStories2", AutomationResolver.ResolverType.Method),
                            Long::class.javaPrimitiveType,
                            object : AbstractMethodHook() {
                                override fun beforeMethod(param: MethodHookParam) {
                                    if (ConfigManager.disableStories.isEnable) {
                                        param.result = false
                                    }
                                }
                            }
                        )
                    } else {
                        HMethod.hookMethod(
                            storiesControllerClass,
                            AutomationResolver.resolve("StoriesController", "hasStories", AutomationResolver.ResolverType.Method),
                            object : AbstractMethodHook() {
                                override fun beforeMethod(param: MethodHookParam) {
                                    if (ConfigManager.disableStories.isEnable) {
                                        param.result = false
                                    }
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
