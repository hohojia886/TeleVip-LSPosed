package com.my.televip.obfuscate.struct

data class FieldInfo(
    @JvmField val className: String,
    @JvmField val original: String,
    @JvmField val resolved: String
)
