package com.my.televip.virtuals

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class VirtualField(
    val name: String,
    val className: String = ""
)

@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class VirtualMethod(
    val name: String,
    val className: String = ""
)
