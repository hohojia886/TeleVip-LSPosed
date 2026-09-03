package com.my.televip

import android.app.Activity
import android.os.Build
import android.os.Bundle
import com.my.televip.Class.ClassLoad
import com.my.televip.Class.ClassNames
import com.my.televip.base.AbstractMethodHook
import com.my.televip.hooks.HMethod
import com.my.televip.utils.Utils
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.IXposedHookZygoteInit
import de.robv.android.xposed.callbacks.XC_LoadPackage
import io.github.libxposed.api.XposedModule
import io.github.libxposed.api.XposedModuleInterface

class MainHook : XposedModule(), IXposedHookLoadPackage, IXposedHookZygoteInit {

    @Volatile
    private var isStart = false

    override fun onModuleLoaded(param: XposedModuleInterface.ModuleLoadedParam) {
        super.onModuleLoaded(param)
    }

    override fun onPackageLoaded(param: XposedModuleInterface.PackageLoadedParam) {
        super.onPackageLoaded(param)
        val packageName = param.packageName
        if (!ClientChecker.ClientType.containsPackage(packageName)) {
            return
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val classLoader = param.defaultClassLoader
            initHooks(packageName, classLoader)
        }
    }

    override fun initZygote(startupParam: IXposedHookZygoteInit.StartupParam) {
        Utils.modulePath = startupParam.modulePath
    }

    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        if (!ClientChecker.ClientType.containsPackage(lpparam.packageName)) {
            return
        }
        initHooks(lpparam.packageName, lpparam.classLoader)
    }

    @Synchronized
    private fun initHooks(packageName: String, classLoader: ClassLoader) {
        Utils.pkgName = packageName
        Utils.classLoader = classLoader

        val launchActivityClass = ClassLoad.getClass(ClassNames.LAUNCH_ACTIVITY)
        if (launchActivityClass != null) {
            HMethod.hookMethod(launchActivityClass, "onCreate", Bundle::class.java, object : AbstractMethodHook() {
                override fun beforeMethod(param: MethodHookParam) {
                    val launchActivity = param.thisObject as? Activity ?: return
                    if (!isStart) {
                        TeleVip.startHook(launchActivity)
                        isStart = true
                    }
                }
            })
        }
    }
}
