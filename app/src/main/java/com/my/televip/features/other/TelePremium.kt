package com.my.televip.features.other

import com.my.televip.Class.ClassLoad
import com.my.televip.Class.ClassNames
import com.my.televip.Clients.ClientManager
import com.my.televip.Configs.ConfigManager
import com.my.televip.base.BaseMethodHook
import com.my.televip.hooks.HMethod
import com.my.televip.logging.Logger
import com.my.televip.obfuscate.Obfuscate
import com.my.televip.utils.Utils
import de.robv.android.xposed.XC_MethodHook
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
                        Obfuscate.getMethodName("UserConfig", "isPremium"),
                        object : BaseMethodHook() {
                            override fun beforeMethod(param: MethodHookParam) {
                                if (ConfigManager.telegramPremium?.isEnable == true) {
                                    param.result = true
                                }
                            }
                        }
                    )
                }

                if (ClientManager.`is`(ClientManager.Client.iMe) || ClientManager.`is`(ClientManager.Client.iMeWeb)) {
                    val forkClass = XposedHelpers.findClassIfExists(
                        "com.iMe.storage.data.locale.prefs.impl.ForkPremiumPreference",
                        Utils.classLoader
                    )
                    if (forkClass != null) {
                        HMethod.hookMethod(
                            forkClass,
                            "isPremium",
                            object : BaseMethodHook() {
                                override fun beforeMethod(param: MethodHookParam) {
                                    if (ConfigManager.telegramPremium?.isEnable == true) {
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
