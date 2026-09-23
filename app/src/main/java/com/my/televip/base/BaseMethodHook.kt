package com.my.televip.base

import com.my.televip.logging.Logger
import de.robv.android.xposed.XC_MethodHook

abstract class BaseMethodHook : XC_MethodHook() {

    @Throws(Throwable::class)
    protected open fun beforeMethod(param: MethodHookParam) {}

    @Throws(Throwable::class)
    protected open fun afterMethod(param: MethodHookParam) {}

    override fun beforeHookedMethod(param: MethodHookParam) {
        try {
            beforeMethod(param)
        } catch (e: Throwable) {
            onError(e)
        }
    }

    override fun afterHookedMethod(param: MethodHookParam) {
        try {
            afterMethod(param)
        } catch (e: Throwable) {
            onError(e)
        }
    }

    protected open fun onError(throwable: Throwable) {
        Logger.e(throwable)
    }
}
