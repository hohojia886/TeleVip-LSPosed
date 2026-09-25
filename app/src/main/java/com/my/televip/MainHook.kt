package com.my.televip

import android.app.Activity
import android.os.Bundle
import com.my.televip.clazz.ClassLoad
import com.my.televip.clazz.ClassNames
import com.my.televip.clients.ClientManager
import com.my.televip.base.BaseMethodHook
import com.my.televip.hooks.HMethod
import com.my.televip.utils.Utils
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.IXposedHookZygoteInit
import de.robv.android.xposed.callbacks.XC_LoadPackage

class MainHook : IXposedHookLoadPackage, IXposedHookZygoteInit {

    private var isStart = false

    override fun initZygote(startupParam: IXposedHookZygoteInit.StartupParam) {
        Utils.modulePath = startupParam.modulePath
    }

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        if (!ClientManager.containsPackage(lpparam.packageName, lpparam.classLoader)) return

        Utils.classLoader = lpparam.classLoader
        Utils.pkgName = lpparam.packageName

        HMethod.hookMethod(
            ClassLoad.getClass(ClassNames.LAUNCH_ACTIVITY),
            "onCreate",
            Bundle::class.java,
            object : BaseMethodHook() {
                override fun beforeMethod(param: MethodHookParam) {
                    Utils.setCurrentActivity(param.thisObject as? Activity)
                    if (!isStart) {
                        TeleVip.startHook()
                        isStart = true
                    }
                }
            }
        )
    }
}
