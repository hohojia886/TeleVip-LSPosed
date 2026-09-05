package com.my.televip.virtuals.tgnet

import com.my.televip.Class.ClassLoad
import com.my.televip.Class.ClassNames
import com.my.televip.obfuscate.AutomationResolver
import de.robv.android.xposed.XposedHelpers

class ConnectionsManager(@JvmField val connectionsManager: Any?) {

    fun sendRequest(obj: Any?, completionBlock: Any?) {
        XposedHelpers.callMethod(
            connectionsManager,
            AutomationResolver.resolve("ConnectionsManager", "sendRequest", AutomationResolver.ResolverType.Method),
            obj,
            completionBlock
        )
    }

    companion object {
        @JvmStatic
        fun getInstance(num: Int): ConnectionsManager {
            return ConnectionsManager(
                XposedHelpers.callStaticMethod(
                    ClassLoad.getClass(ClassNames.CONNECTIONS_MANAGER),
                    AutomationResolver.resolve("ConnectionsManager", "getInstance", AutomationResolver.ResolverType.Method),
                    num
                )
            )
        }
    }
}
