package com.my.televip.utils

object Utils {
    @JvmField var pkgName: String? = null
    @JvmField var modulePath: String? = null
    @JvmField var classLoader: ClassLoader? = null
    const val issue = "Your Telegram client may be an incompatible version with TeleVip. Please download the latest version that is compatible with TeleVip."

    @JvmStatic
    fun <T> castList(obj: Any?, clazz: Class<T>): ArrayList<T> {
        val result = ArrayList<T>()
        if (obj is ArrayList<*>) {
            for (o in obj) {
                if (o != null) {
                    result.add(clazz.cast(o)!!)
                }
            }
        }
        return result
    }

    @JvmStatic
    fun getFieldAsString(value: Any?): String? {
        if (value == null) return null
        if (value is CharSequence) return value.toString()
        return value.toString()
    }
}
