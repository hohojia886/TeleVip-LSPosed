package com.televip.SettingsAdapter

object Bridge {

    @JvmStatic
    fun getRow(pos: Int): Int = 0

    @JvmStatic
    fun getRowCount(): Int = 0

    @JvmStatic
    fun onBindViewHolder(holder: Any?, position: Int, viewType: Int) {}

    @JvmStatic
    fun log(log: String?) {}
}
