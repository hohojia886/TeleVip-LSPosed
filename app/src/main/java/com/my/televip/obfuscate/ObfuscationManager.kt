package com.my.televip.obfuscate

import com.my.televip.clients.ClientManager
import com.my.televip.obfuscate.struct.ClientObfuscationData
import com.my.televip.utils.JsonAssetReader
import java.util.EnumMap

object ObfuscationManager {

    private val cache: MutableMap<ClientManager.Client, ClientObfuscationData> =
        EnumMap(ClientManager.Client::class.java)

    @Volatile
    private var currentClientData: ClientObfuscationData? = null

    @Volatile
    private var attemptedLoad = false

    @JvmStatic
    fun current(): ClientObfuscationData? {
        currentClientData?.let { return it }
        if (attemptedLoad) return null

        synchronized(this) {
            currentClientData?.let { return it }
            attemptedLoad = true

            val type = ClientManager.getCurrent() ?: return null
            currentClientData = load(type)
            return currentClientData
        }
    }

    private fun load(type: ClientManager.Client): ClientObfuscationData? {
        if (cache.containsKey(type)) return cache[type]

        var data: ClientObfuscationData? = null
        val clientPath = "assets/clients/${type.name}.json"
        val clientJson = JsonAssetReader.readRaw(clientPath, false)

        val clientAliasPath = "assets/clients/Alias/${type.name}.json"
        val clientAliasJson = JsonAssetReader.readRaw(clientAliasPath, false)

        if (clientJson != null && clientAliasJson != null) {
            data = ClientObfuscationData.parse(clientJson, clientAliasJson)
        }

        if (data != null) {
            cache[type] = data
        }
        return data
    }
}
