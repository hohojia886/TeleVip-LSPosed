package com.my.televip.base

import com.my.televip.logging.Logger
import de.robv.android.xposed.XC_MethodHook

abstract class AbstractMethodHook : XC_MethodHook() {

    @Throws(Throwable::class)
    protected open fun beforeMethod(param: MethodHookParam) {
    }

    @Throws(Throwable::class)
    protected open fun afterMethod(param: MethodHookParam) {
    }

    @Throws(Throwable::class)
    override fun beforeHookedMethod(param: MethodHookParam) {
        super.beforeHookedMethod(param)
        try {
            beforeMethod(param)
        } catch (throwable: Throwable) {
            Logger.e(throwable)
        }
    }

    @Throws(Throwable::class)
    override fun afterHookedMethod(param: MethodHookParam) {
        super.afterHookedMethod(param)
        try {
            afterMethod(param)
        } catch (throwable: Throwable) {
            Logger.e(throwable)
        }
    }
}
