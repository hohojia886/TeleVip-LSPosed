package com.my.televip.virtuals.tgnet

import com.my.televip.Class.ClassLoad
import com.my.televip.Class.ClassNames
import com.my.televip.obfuscate.AutomationResolver
import de.robv.android.xposed.XposedHelpers

class NativeByteBuffer {
    @JvmField val nativeByteBuffer: Any?

    constructor(obj: Any?) {
        nativeByteBuffer = obj
    }

    constructor(calculate: Boolean) {
        nativeByteBuffer = XposedHelpers.newInstance(
            ClassLoad.getClass(ClassNames.NATIVE_BYTE_BUFFER),
            calculate
        )
    }

    fun reuse() {
        XposedHelpers.callMethod(
            nativeByteBuffer,
            AutomationResolver.resolve("NativeByteBuffer", "reuse", AutomationResolver.ResolverType.Method)
        )
    }

    fun readInt32(exception: Boolean): Int {
        return XposedHelpers.callMethod(
            nativeByteBuffer,
            AutomationResolver.resolve("NativeByteBuffer", "readInt32", AutomationResolver.ResolverType.Method),
            exception
        ) as Int
    }

    fun position(i: Int) {
        XposedHelpers.callMethod(
            nativeByteBuffer,
            AutomationResolver.resolve("NativeByteBuffer", "position", AutomationResolver.ResolverType.Method),
            i
        )
    }

    fun writeInt32(i: Int) {
        XposedHelpers.callMethod(
            nativeByteBuffer,
            AutomationResolver.resolve("NativeByteBuffer", "writeInt32", AutomationResolver.ResolverType.Method),
            i
        )
    }
}
