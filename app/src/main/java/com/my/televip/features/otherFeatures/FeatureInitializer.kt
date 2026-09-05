package com.my.televip.features.otherFeatures

import android.content.Context
import com.my.televip.Class.ClassLoad
import com.my.televip.Class.ClassNames
import com.my.televip.base.AbstractMethodHook
import com.my.televip.hooks.HMethod
import com.my.televip.logging.Logger
import com.my.televip.obfuscate.AutomationResolver
import com.my.televip.utils.Utils
import de.robv.android.xposed.XposedHelpers

object FeatureInitializer {
    @JvmStatic
    fun init(context: Context) {
        try {
            if (!FeatureStateManager.isChatEnabled || !FeatureStateManager.isProfileEnabled) {
                val actionBarClass = XposedHelpers.findClassIfExists(
                    AutomationResolver.resolve("org.telegram.ui.ActionBar.ActionBar"),
                    Utils.classLoader
                )
                val actionBarMenuClickClass = ClassLoad.getClass(ClassNames.ACTION_BAR_MENU_ON_ITEM_CLICK)
                if (actionBarClass != null && actionBarMenuClickClass != null) {
                    HMethod.hookMethod(
                        actionBarClass,
                        AutomationResolver.resolve("ActionBar", "setActionBarMenuOnItemClick", AutomationResolver.ResolverType.Method),
                        actionBarMenuClickClass,
                        object : AbstractMethodHook() {
                            override fun beforeMethod(param: MethodHookParam) {
                                val clazzObj = param.args[0] ?: return
                                val name = clazzObj.javaClass.name

                                if (name.contains("ChatActivity") && !FeatureStateManager.isChatEnabled) {
                                    FeatureStateManager.saveChat(name)
                                    ChatHook.init(context, name)
                                }

                                if (name.contains("ProfileActivity") && !FeatureStateManager.isProfileEnabled) {
                                    FeatureStateManager.saveProfile(name)
                                    ProfileHook.init(context, name)
                                }
                            }
                        }
                    )
                }
            } else {
                val chatClass = FeatureStateManager.chatClass
                if (chatClass != null) {
                    ChatHook.init(context, chatClass)
                }
                val profileClass = FeatureStateManager.profileClass
                if (profileClass != null) {
                    ProfileHook.init(context, profileClass)
                }
            }
        } catch (t: Throwable) {
            Logger.e(t)
        }
    }
}
