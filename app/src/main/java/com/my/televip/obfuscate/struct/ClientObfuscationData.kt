package com.my.televip.obfuscate.struct

import org.json.JSONArray
import org.json.JSONObject

class ClientObfuscationData {

    private val classMap = HashMap<String, String>()
    private val fieldMap = HashMap<String, String>()
    private val methodMap = HashMap<String, String>()
    private val methodAliasMap = HashMap<String, String>()

    private fun parseClasses(arr: JSONArray?) {
        if (arr == null) return
        for (i in 0 until arr.length()) {
            val o = arr.optJSONObject(i) ?: continue
            val original = o.optString("o", null) ?: continue
            val resolved = o.optString("r", null) ?: continue
            classMap[original] = resolved
        }
    }

    private fun parseFields(arr: JSONArray?) {
        if (arr == null) return
        for (i in 0 until arr.length()) {
            val o = arr.optJSONObject(i) ?: continue
            val className = o.optString("c", null) ?: continue
            val original = o.optString("o", null) ?: continue
            val resolved = o.optString("r", null) ?: continue
            fieldMap["$className#$original"] = resolved
        }
    }

    private fun parseMethods(arr: JSONArray?) {
        if (arr == null) return
        for (i in 0 until arr.length()) {
            val o = arr.optJSONObject(i) ?: continue
            val className = o.optString("c", null) ?: continue
            val original = o.optString("o", null) ?: continue
            val resolved = o.optString("r", null) ?: continue
            methodMap["$className#$original"] = resolved
        }
    }

    private fun parseMethodAlias(obj: JSONObject?) {
        if (obj == null) return
        val keys = obj.keys()
        while (keys.hasNext()) {
            val key = keys.next()
            val value = obj.optString(key, null)
            if (value != null) {
                methodAliasMap[key] = value
            }
        }
    }

    fun resolveClass(original: String): String {
        return classMap[original] ?: original
    }

    fun resolveField(className: String, original: String): String {
        return fieldMap["$className#$original"] ?: original
    }

    fun resolveMethod(className: String, original: String): String {
        val aliased = methodAliasMap["$className#$original"] ?: original
        return methodMap["$className#$aliased"] ?: aliased
    }

    companion object {
        @JvmStatic
        fun parse(clientJson: String, clientAliasJson: String): ClientObfuscationData {
            val data = ClientObfuscationData()
            try {
                val root = JSONObject(clientJson)
                data.parseClasses(root.optJSONArray("classes"))
                data.parseFields(root.optJSONArray("fields"))
                data.parseMethods(root.optJSONArray("methods"))
                val aliasRoot = JSONObject(clientAliasJson)
                data.parseMethodAlias(aliasRoot.optJSONObject("methodAlias"))
            } catch (t: Throwable) {
                t.printStackTrace()
            }
            return data
        }
    }
}
