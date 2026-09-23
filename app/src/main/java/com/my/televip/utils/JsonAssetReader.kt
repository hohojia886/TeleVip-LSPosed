package com.my.televip.utils

import com.my.televip.logging.Logger
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.util.concurrent.ConcurrentHashMap
import java.util.zip.ZipFile

object JsonAssetReader {

    private val rawCache = ConcurrentHashMap<String, String>()

    @JvmStatic
    fun readObject(assetPath: String): JSONObject? {
        val raw = readRaw(assetPath, true) ?: return null
        return try {
            JSONObject(raw)
        } catch (e: Throwable) {
            Logger.e(e)
            null
        }
    }

    @JvmStatic
    @JvmOverloads
    fun readRaw(assetPath: String, errorLog: Boolean = true): String? {
        val normalized = normalize(assetPath)

        val cached = rawCache[normalized]
        if (cached != null) return cached

        val modulePath = Utils.modulePath ?: return null

        try {
            ZipFile(modulePath).use { zipFile ->
                val entry = zipFile.getEntry(normalized)
                if (entry == null) {
                    if (errorLog) Logger.w("JsonAssetReader: entry not found -> $normalized")
                    return null
                }

                zipFile.getInputStream(entry).use { inputStream ->
                    val content = readFully(inputStream)
                    rawCache[normalized] = content
                    return content
                }
            }
        } catch (e: Throwable) {
            Logger.e(e)
            return null
        }
    }

    @JvmStatic
    fun listFiles(dirPath: String, extension: String): List<String> {
        val result = ArrayList<String>()
        var normalizedDir = normalize(dirPath)
        if (!normalizedDir.endsWith("/")) normalizedDir += "/"

        val modulePath = Utils.modulePath ?: return result

        try {
            ZipFile(modulePath).use { zipFile ->
                val entries = zipFile.entries()
                while (entries.hasMoreElements()) {
                    val entry = entries.nextElement()
                    val name = entry.name

                    if (name.startsWith(normalizedDir) && name.endsWith(extension)) {
                        val fileName = name.substring(name.lastIndexOf('/') + 1)
                        result.add(fileName)
                    }
                }
            }
        } catch (e: Throwable) {
            Logger.e(e)
        }

        return result
    }

    private fun normalize(path: String): String {
        return if (path.startsWith("/")) path.substring(1) else path
    }

    private fun readFully(inputStream: InputStream): String {
        val baos = ByteArrayOutputStream()
        val buffer = ByteArray(4096)
        var bytesRead: Int
        while (inputStream.read(buffer).also { bytesRead = it } != -1) {
            baos.write(buffer, 0, bytesRead)
        }
        return baos.toString("UTF-8")
    }
}
