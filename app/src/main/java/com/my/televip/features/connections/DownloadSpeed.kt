package com.my.televip.features.connections

import com.my.televip.Class.ClassLoad
import com.my.televip.Class.ClassNames
import com.my.televip.Configs.ConfigManager
import com.my.televip.base.BaseMethodHook
import com.my.televip.hooks.HMethod
import com.my.televip.logging.Logger
import com.my.televip.obfuscate.Obfuscate
import com.my.televip.virtuals.messenger.FileLoadOperation

object DownloadSpeed {

    @JvmField
    var isEnable: Boolean = false

    @JvmStatic
    fun init() {
        try {
            if (!isEnable) {
                isEnable = true
                val operationClass = ClassLoad.getClass(ClassNames.FILE_LOAD_OPERATION)
                if (operationClass != null) {
                    HMethod.hookMethod(
                        operationClass,
                        Obfuscate.getMethodName("FileLoadOperation", "updateParams"),
                        object : BaseMethodHook() {
                            override fun afterMethod(param: MethodHookParam) {
                                if (ConfigManager.downloadSpeed?.isEnable == true) {
                                    val fileLoadOperation = FileLoadOperation(param.thisObject)

                                    val downloadChunkSizeBig = 1024 * 512
                                    val maxDownloadRequests = 8
                                    val defaultMaxFileSize = 1024L * 1024L * 2000L
                                    val maxCdnParts = (defaultMaxFileSize / downloadChunkSizeBig).toInt()

                                    fileLoadOperation.setDownloadChunkSizeBig(downloadChunkSizeBig)
                                    fileLoadOperation.setMaxDownloadRequests(maxDownloadRequests)
                                    fileLoadOperation.setMaxDownloadRequestsBig(maxDownloadRequests)
                                    fileLoadOperation.setMaxCdnParts(maxCdnParts)
                                    param.result = null
                                }
                            }
                        }
                    )
                }
            }
        } catch (t: Throwable) {
            Logger.e(t)
        }
    }
}
