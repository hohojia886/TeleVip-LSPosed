package com.my.televip.Configs

class ConfigItem {

    companion object {
        const val HEADER = 0
        const val SWITCH = 1
        const val TEXT = 2
        const val DIVIDER = 3
        const val INFO = 4
        const val EXPANDABLE_SWITCH = 5
    }

    val type: Int
    var key: String? = null
    var value: String? = null
    var isRestartRequired: Boolean = false
    var isEnable: Boolean = false
    private var runnable: Runnable? = null
    var children: List<ConfigItem>? = null

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
        this.isEnable = children.any { it.isEnable }
    }

    constructor(type: Int, key: String?) {
        this.type = type
        this.key = key
    }

    constructor(type: Int) {
        this.type = type
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
}
