package com.my.televip.language

import com.my.televip.logging.Logger
import com.my.televip.utils.JsonAssetReader
import com.my.televip.virtuals.messenger.LocaleController
import org.json.JSONObject

object Translator {

    private val langMap = HashMap<String, JSONObject>()
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
        val files = JsonAssetReader.listFiles("assets/lang/", ".json")
        for (fileName in files) {
            val langCode = fileName.substring(0, fileName.lastIndexOf('.'))
            val json = JsonAssetReader.readObject("assets/lang/$fileName")
            if (json != null) {
                langMap[langCode] = json
            }
        }
    }

    @JvmStatic
    fun get(key: String): String {
        val controller = localeController ?: return key
        try {
            val currentLocale = controller.currentLocale ?: return key
            val lang = currentLocale.language
            val langJson = langMap[lang]

            if (langJson != null && langJson.has(key)) {
                return langJson.optString(key)
            }
            val enJson = langMap["en"]
            return if (enJson != null && enJson.has(key)) {
                enJson.optString(key)
            } else {
                key
            }
        } catch (e: Throwable) {
            Logger.e(e)
        }
        return key
    }

    @JvmStatic
    fun get(key: String, vararg args: Any?): String {
        val controller = localeController ?: return key
        try {
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

            if (args.isNotEmpty() && text != null) {
                return String.format(text, *args)
            }
            return text ?: key
        } catch (e: Throwable) {
            Logger.e(e)
        }
        return key
    }
}
