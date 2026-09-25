package com.my.televip.features.otherfeatures

import com.my.televip.clazz.ClassLoad
import com.my.televip.clazz.ClassNames
import com.my.televip.base.BaseMethodHook
import com.my.televip.hooks.HMethod
import com.my.televip.logging.Logger
import com.my.televip.obfuscate.Obfuscate
import com.my.televip.utils.Utils
import de.robv.android.xposed.XposedHelpers

object FeatureInitializer {

    @JvmStatic
    fun init() {
        try {
            if (!FeatureStateManager.isChatEnabled() || !FeatureStateManager.isProfileEnabled()) {
                val actionBarClass = XposedHelpers.findClassIfExists(
                    Obfuscate.getClassName("org.telegram.ui.ActionBar.ActionBar"),
                    Utils.classLoader
                )

                if (actionBarClass != null) {
                    HMethod.hookMethod(
                        actionBarClass,
                        Obfuscate.getMethodName("ActionBar", "setActionBarMenuOnItemClick"),
                        ClassLoad.getClass(ClassNames.ACTION_BAR_MENU_ON_ITEM_CLICK)!!,
                        object : BaseMethodHook() {
                            override fun beforeMethod(param: MethodHookParam) {
                                val clazz = param.args[0] ?: return
                                val name = clazz.javaClass.name

                                if (name.contains("ChatActivity") && !FeatureStateManager.isChatEnabled()) {
                                    FeatureStateManager.saveChat(name)
                                    ChatHook.init(name)
                                }

                                if (name.contains("ProfileActivity") && !FeatureStateManager.isProfileEnabled()) {
                                    FeatureStateManager.saveProfile(name)
                                    ProfileHook.init(name)
                                }
                            }
                        }
                    )
                }
            } else {
                ChatHook.init(FeatureStateManager.getChatClass())
                ProfileHook.init(FeatureStateManager.getProfileClass())
            }
        } catch (t: Throwable) {
            Logger.e(t)
        }
    }
}
