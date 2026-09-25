package com.my.televip.configs

class ConfigItem {

    val type: Int
    var key: String? = null
        private set
    var value: String? = null
        private set
    var isRestartRequired: Boolean = false
        private set
    var isEnable: Boolean = false
        private set
    private var runnable: Runnable? = null
    var children: List<ConfigItem>? = null
        private set

    constructor(type: Int, key: String?, value: String?, enable: Boolean, runnable: Runnable?) {
        this.type = type
        this.key = key
        this.value = value
        this.isEnable = enable
        this.runnable = runnable
    }

    constructor(type: Int, key: String?, restartRequired: Boolean, enable: Boolean, runnable: Runnable?) {
        this.type = type
        this.key = key
        this.isRestartRequired = restartRequired
        this.isEnable = enable
        this.runnable = runnable
    }

    constructor(type: Int, key: String?, enable: Boolean, runnable: Runnable?) {
        this.type = type
        this.key = key
        this.isEnable = enable
        this.runnable = runnable
    }

    constructor(type: Int, key: String?, children: List<ConfigItem>) {
        this.type = type
        this.key = key
        this.children = children
        var any = false
        for (child in children) {
            if (child.isEnable) {
                any = true
                break
            }
        }
        this.isEnable = any
    }

    constructor(type: Int, key: String?) {
        this.type = type
        this.key = key
    }

    constructor(type: Int) {
        this.type = type
    }

    fun setEnable(value: Boolean) {
        this.isEnable = value
        key?.let { ConfigPreferences.putBoolean(it, value) }
    }

    fun getCustomCalendar(): Int {
        return key?.let { ConfigPreferences.getInt("${it}Int") } ?: 0
    }

    fun setCustomCalendar(value: Int) {
        key?.let { ConfigPreferences.putInt("${it}Int", value) }
    }

    fun run() {
        runnable?.run()
    }

    companion object {
        const val HEADER: Int = 0
        const val SWITCH: Int = 1
        const val TEXT: Int = 2
        const val DIVIDER: Int = 3
        const val INFO: Int = 4
        const val EXPANDABLE_SWITCH: Int = 5
    }
}
