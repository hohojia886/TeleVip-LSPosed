package com.my.televip.language

import com.my.televip.logging.Logger
import com.my.televip.utils.Utils
import com.my.televip.virtuals.messenger.LocaleController
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.InputStream
import java.util.concurrent.ConcurrentHashMap
import java.util.zip.ZipFile

object Translator {

    private val langMap = ConcurrentHashMap<String, JSONObject>()
    private var localeController: LocaleController? = null

    @JvmStatic
    fun init() {
        try {
            loadAllLanguages()
            localeController = LocaleController()
        } catch (e: Throwable) {
            Logger.e(e)
        }
    }

    private fun loadAllLanguages() {
        try {
            val modulePath = Utils.modulePath ?: return
            ZipFile(modulePath).use { zipFile ->
                val entries = zipFile.entries()
                while (entries.hasMoreElements()) {
                    val entry = entries.nextElement()
                    val name = entry.name
                    if (name.startsWith("assets/lang/") && name.endsWith(".json")) {
                        val langCode = name.substring(name.lastIndexOf("/") + 1, name.lastIndexOf("."))
                        zipFile.getInputStream(entry).use { isStream ->
                            val json = readFully(isStream)
                            langMap[langCode] = JSONObject(json)
                        }
                    }
                }
            }
        } catch (e: Throwable) {
            Logger.e(e)
        }
    }

    private fun readFully(isStream: InputStream): String {
        val baos = ByteArrayOutputStream()
        val buffer = ByteArray(4096)
        var bytesRead: Int
        while (isStream.read(buffer).also { bytesRead = it } != -1) {
            baos.write(buffer, 0, bytesRead)
        }
        return baos.toString("UTF-8")
    }

    @JvmStatic
    fun get(key: String): String {
        val controller = localeController ?: return key
        return try {
            val currentLocale = controller.currentLocale ?: return key
            val lang = currentLocale.language
            val langJson = langMap[lang]
            if (langJson != null && langJson.has(key)) {
                return langJson.optString(key)
            }
            val enJson = langMap["en"]
            if (enJson != null && enJson.has(key)) {
                enJson.optString(key)
            } else {
                key
            }
        } catch (e: Throwable) {
            Logger.e(e)
            key
        }
    }

    @JvmStatic
    fun get(key: String, vararg args: Any): String {
        val controller = localeController ?: return key
        return try {
            val currentLocale = controller.currentLocale ?: return key
            val lang = currentLocale.language
            var text: String? = null
            val langJson = langMap[lang]
            if (langJson != null && langJson.has(key)) {
                text = langJson.optString(key)
            }
            if (text == null) {
                val enJson = langMap["en"]
                text = if (enJson != null && enJson.has(key)) {
                    enJson.optString(key)
                } else {
                    key
                }
            }
            val nonNullText = text ?: key
            if (args.isNotEmpty()) {
                String.format(nonNullText, *args)
            } else {
                nonNullText
            }
        } catch (e: Throwable) {
            Logger.e(e)
            key
        }
    }
}
