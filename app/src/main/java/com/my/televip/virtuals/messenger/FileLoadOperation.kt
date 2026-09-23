package com.my.televip.virtuals.messenger

import com.my.televip.obfuscate.Obfuscate
import de.robv.android.xposed.XposedHelpers

class FileLoadOperation(private val fileOperation: Any?) {

    fun setDownloadChunkSizeBig(v: Int) {
        val target = fileOperation ?: return
        XposedHelpers.setIntField(target, Obfuscate.getFieldName("FileLoadOperation", "downloadChunkSizeBig"), v)
    }

    fun setMaxDownloadRequests(v: Int) {
        val target = fileOperation ?: return
        XposedHelpers.setIntField(target, Obfuscate.getFieldName("FileLoadOperation", "maxDownloadRequests"), v)
    }

    fun setMaxDownloadRequestsBig(v: Int) {
        val target = fileOperation ?: return
        XposedHelpers.setIntField(target, Obfuscate.getFieldName("FileLoadOperation", "maxDownloadRequestsBig"), v)
    }

    fun setMaxCdnParts(v: Int) {
        val target = fileOperation ?: return
        XposedHelpers.setIntField(target, Obfuscate.getFieldName("FileLoadOperation", "maxCdnParts"), v)
    }
}
