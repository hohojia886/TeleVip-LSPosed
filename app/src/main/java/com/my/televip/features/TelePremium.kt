package com.my.televip.features

import com.my.televip.Class.ClassLoad
import com.my.televip.Class.ClassNames
import com.my.televip.ClientChecker
import com.my.televip.Configs.ConfigManager
import com.my.televip.base.AbstractMethodHook
import com.my.televip.hooks.HMethod
import com.my.televip.logging.Logger
import com.my.televip.obfuscate.AutomationResolver
import com.my.televip.utils.Utils
import de.robv.android.xposed.XposedHelpers

object TelePremium {

    @JvmField
    var isEnable: Boolean = false

    @JvmStatic
    fun init() {
        try {
            if (!isEnable) {
                isEnable = true

                val userConfigClass = ClassLoad.getClass(ClassNames.USER_CONFIG)
                if (userConfigClass != null) {
                    HMethod.hookMethod(
                        userConfigClass,
                        AutomationResolver.resolve("UserConfig", "isPremium", AutomationResolver.ResolverType.Method),
                        object : AbstractMethodHook() {
                            override fun beforeMethod(param: MethodHookParam) {
                                if (ConfigManager.telegramPremium.isEnable) {
                                    param.result = true
                                }
                            }
                        }
                    )
                }

                if (ClientChecker.check(ClientChecker.ClientType.iMe) || ClientChecker.check(ClientChecker.ClientType.iMeWeb)) {
                    val forkPremiumPrefClass = XposedHelpers.findClassIfExists("com.iMe.storage.data.locale.prefs.impl.ForkPremiumPreference", Utils.classLoader)
                    if (forkPremiumPrefClass != null) {
                        HMethod.hookMethod(
                            forkPremiumPrefClass,
                            "isPremium",
                            object : AbstractMethodHook() {
                                override fun beforeMethod(param: MethodHookParam) {
                                    if (ConfigManager.telegramPremium.isEnable) {
                                        param.result = true
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
