package com.my.televip.virtuals.ui

import com.my.televip.clazz.ClassLoad
import com.my.televip.clazz.ClassNames
import com.my.televip.base.BaseMethodHook
import com.my.televip.hooks.HMethod
import com.my.televip.logging.Logger
import com.my.televip.obfuscate.Obfuscate
import de.robv.android.xposed.XposedHelpers

class SecretMediaViewer(@JvmField val secretMediaViewer: Any?) {

    companion object {
        @JvmStatic
        fun openMedia() {
            try {
                val smvClass = ClassLoad.getClass(ClassNames.SECRET_MEDIA_VIEWER)
                if (smvClass != null) {
                    HMethod.hookMethod(smvClass, Obfuscate.getMethodName("SecretMediaViewer", "isSecretMedia"), object : BaseMethodHook() {
                        override fun beforeMethod(param: MethodHookParam) {
                            param.result = false
                        }
                    })
                    HMethod.hookMethod(smvClass, Obfuscate.getMethodName("SecretMediaViewer", "closePhoto"), Boolean::class.javaPrimitiveType!!, Boolean::class.javaPrimitiveType!!, object : BaseMethodHook() {
                        override fun beforeMethod(param: MethodHookParam) {
                            val thisObject = param.thisObject
                            XposedHelpers.setObjectField(thisObject, Obfuscate.getFieldName("SecretMediaViewer", "onClose"), null)
                        }
                    })
                }
            } catch (e: Throwable) {
                Logger.e(e)
            }
        }
    }
}
