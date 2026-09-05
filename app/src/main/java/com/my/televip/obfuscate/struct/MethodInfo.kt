package com.my.televip.obfuscate.struct

data class MethodInfo(
    @JvmField val className: String,
    @JvmField val original: String,
    @JvmField val resolved: String
)
