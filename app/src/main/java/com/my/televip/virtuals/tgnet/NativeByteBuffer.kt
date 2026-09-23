package com.my.televip.virtuals.tgnet

import com.my.televip.Class.ClassLoad
import com.my.televip.Class.ClassNames
import com.my.televip.obfuscate.Obfuscate
import de.robv.android.xposed.XposedHelpers

class NativeByteBuffer {

    @JvmField
    var nativeByteBuffer: Any? = null

    constructor(obj: Any?) {
        nativeByteBuffer = obj
    }

    constructor(calculate: Boolean) {
        val nbClass = ClassLoad.getClass(ClassNames.NATIVE_BYTE_BUFFER)
        nativeByteBuffer = XposedHelpers.newInstance(nbClass, calculate)
    }

    fun reuse() {
        val target = nativeByteBuffer ?: return
        XposedHelpers.callMethod(target, Obfuscate.getMethodName("NativeByteBuffer", "reuse"))
    }

    fun readInt32(exception: Boolean): Int {
        val target = nativeByteBuffer ?: return 0
        return XposedHelpers.callMethod(target, Obfuscate.getMethodName("NativeByteBuffer", "readInt32"), exception) as? Int ?: 0
    }

    fun position(i: Int) {
        val target = nativeByteBuffer ?: return
        XposedHelpers.callMethod(target, Obfuscate.getMethodName("NativeByteBuffer", "position"), i)
    }

    fun writeInt32(i: Int) {
        val target = nativeByteBuffer ?: return
        XposedHelpers.callMethod(target, Obfuscate.getMethodName("NativeByteBuffer", "writeInt32"), i)
    }
}
