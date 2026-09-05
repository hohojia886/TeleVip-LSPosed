package com.my.televip.virtuals.messenger

import com.my.televip.obfuscate.AutomationResolver
import de.robv.android.xposed.XposedHelpers

class FileLoadOperation(private val fileOperation: Any?) {
    fun setDownloadChunkSizeBig(v: Int) {
        XposedHelpers.setIntField(
            fileOperation,
            AutomationResolver.resolve("FileLoadOperation", "downloadChunkSizeBig", AutomationResolver.ResolverType.Field),
            v
        )
    }

    fun setMaxDownloadRequests(v: Int) {
        XposedHelpers.setIntField(
            fileOperation,
            AutomationResolver.resolve("FileLoadOperation", "maxDownloadRequests", AutomationResolver.ResolverType.Field),
            v
        )
    }

    fun setMaxDownloadRequestsBig(v: Int) {
        XposedHelpers.setIntField(
            fileOperation,
            AutomationResolver.resolve("FileLoadOperation", "maxDownloadRequestsBig", AutomationResolver.ResolverType.Field),
            v
        )
    }

    fun setMaxCdnParts(v: Int) {
        XposedHelpers.setIntField(
            fileOperation,
            AutomationResolver.resolve("FileLoadOperation", "maxCdnParts", AutomationResolver.ResolverType.Field),
            v
        )
    }
}
