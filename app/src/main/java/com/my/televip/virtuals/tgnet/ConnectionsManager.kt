package com.my.televip.virtuals.tgnet

import com.my.televip.clazz.ClassLoad
import com.my.televip.clazz.ClassNames
import com.my.televip.obfuscate.Obfuscate
import com.my.televip.virtuals.messenger.UserConfig
import de.robv.android.xposed.XposedHelpers

class ConnectionsManager(@JvmField val connectionsManager: Any?) {

    fun sendRequest(obj: Any?, completionBlock: Any?) {
        val target = connectionsManager ?: return
        XposedHelpers.callMethod(target, Obfuscate.getMethodName("ConnectionsManager", "sendRequest"), obj, completionBlock)
    }

    fun getCurrentTime(): Int {
        val target = connectionsManager ?: return 0
        return XposedHelpers.callMethod(target, Obfuscate.getMethodName("ConnectionsManager", "getCurrentTime")) as? Int ?: 0
    }

    companion object {
        @JvmStatic
        fun getInstance(num: Int): ConnectionsManager {
            val cmClass = ClassLoad.getClass(ClassNames.CONNECTIONS_MANAGER)
            val instance = XposedHelpers.callStaticMethod(cmClass, Obfuscate.getMethodName("ConnectionsManager", "getInstance"), num)
            return ConnectionsManager(instance)
        }

        @JvmStatic
        fun getConnectionsManager(): ConnectionsManager {
            return getInstance(UserConfig.getSelectedAccount())
        }
    }
}
