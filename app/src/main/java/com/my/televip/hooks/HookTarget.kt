package com.my.televip.hooks

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION)
annotation class HookTarget(
    val clazz: String,
    val method: String
)
