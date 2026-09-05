package com.my.televip.obfuscate.struct

import com.my.televip.ClientChecker
import com.my.televip.utils.Utils
import java.lang.reflect.Method

class ResolverRegistry(private val clazz: Class<*>?) {

    @JvmField var finalField: Class<*>? = null
    @JvmField var finalMethod: Class<*>? = null
    @JvmField var finalParameter: Class<*>? = null
    @JvmField var finalClass: Class<*>? = null

    private var resolveMethodNameM: Method? = null
    private var classHasM: Method? = null
    private var classResolveM: Method? = null
    private var fieldHasM: Method? = null
    private var fieldResolveM: Method? = null
    private var methodHasM: Method? = null
    private var methodResolveM: Method? = null
    private var parameterHasM: Method? = null
    private var parameterResolveM: Method? = null
    private var loadParameterM: Method? = null

    init {
        if (clazz != null) {
            try {
                var fieldResolverClass: Class<*>? = null
                var methodResolverClass: Class<*>? = null
                var parameterResolverClass: Class<*>? = null
                var classResolverClass: Class<*>? = null
                for (inner in clazz.declaredClasses) {
                    when (inner.simpleName) {
                        "FieldResolver" -> fieldResolverClass = inner
                        "MethodResolver" -> methodResolverClass = inner
                        "ParameterResolver" -> parameterResolverClass = inner
                        "ClassResolver" -> classResolverClass = inner
                    }
                }

                finalField = fieldResolverClass
                finalMethod = methodResolverClass
                finalParameter = parameterResolverClass
                finalClass = classResolverClass

                runCatching { resolveMethodNameM = clazz?.getMethod("resolveMethodName", String::class.java, String::class.java) }
                runCatching { loadParameterM = clazz?.getMethod("loadParameter") }

                finalClass?.let {
                    runCatching { classHasM = it.getMethod("has", String::class.java) }
                    runCatching { classResolveM = it.getMethod("resolve", String::class.java) }
                }
                finalField?.let {
                    runCatching { fieldHasM = it.getMethod("has", String::class.java, String::class.java) }
                    runCatching { fieldResolveM = it.getMethod("resolve", String::class.java, String::class.java) }
                }
                finalMethod?.let {
                    runCatching { methodHasM = it.getMethod("has", String::class.java, String::class.java) }
                    runCatching { methodResolveM = it.getMethod("resolve", String::class.java, String::class.java) }
                }
                finalParameter?.let {
                    runCatching { parameterHasM = it.getMethod("has", String::class.java) }
                    runCatching { parameterResolveM = it.getMethod("resolve", String::class.java) }
                }
            } catch (_: Throwable) {}
        }
    }

    fun resolveMethodName(className: String, name: String): String {
        return try {
            if (resolveMethodNameM != null) {
                (resolveMethodNameM!!.invoke(null, className, name) as? String) ?: name
            } else if (clazz != null) {
                (clazz.getMethod("resolveMethodName", String::class.java, String::class.java).invoke(null, className, name) as? String) ?: name
            } else {
                name
            }
        } catch (_: Throwable) {
            name
        }
    }

    fun hasClass(className: String): Boolean {
        return try {
            if (classHasM != null) {
                (classHasM!!.invoke(null, className) as? Boolean) == true
            } else if (finalClass != null) {
                (finalClass!!.getMethod("has", String::class.java).invoke(null, className) as? Boolean) == true
            } else {
                false
            }
        } catch (_: Throwable) {
            false
        }
    }

    fun resolveClass(className: String): String? {
        return try {
            if (classResolveM != null) {
                classResolveM!!.invoke(null, className) as? String
            } else if (finalClass != null) {
                finalClass!!.getMethod("resolve", String::class.java).invoke(null, className) as? String
            } else {
                null
            }
        } catch (_: Throwable) {
            null
        }
    }

    fun hasField(className: String, name: String): Boolean {
        return try {
            if (fieldHasM != null) {
                (fieldHasM!!.invoke(null, className, name) as? Boolean) == true
            } else if (finalField != null) {
                (finalField!!.getMethod("has", String::class.java, String::class.java).invoke(null, className, name) as? Boolean) == true
            } else {
                false
            }
        } catch (_: Throwable) {
            false
        }
    }

    fun resolveField(className: String, name: String): String? {
        return try {
            if (fieldResolveM != null) {
                fieldResolveM!!.invoke(null, className, name) as? String
            } else if (finalField != null) {
                finalField!!.getMethod("resolve", String::class.java, String::class.java).invoke(null, className, name) as? String
            } else {
                null
            }
        } catch (_: Throwable) {
            null
        }
    }

    fun hasMethod(className: String, name: String): Boolean {
        return try {
            if (methodHasM != null) {
                (methodHasM!!.invoke(null, className, name) as? Boolean) == true
            } else if (finalMethod != null) {
                (finalMethod!!.getMethod("has", String::class.java, String::class.java).invoke(null, className, name) as? Boolean) == true
            } else {
                false
            }
        } catch (_: Throwable) {
            false
        }
    }

    fun resolveMethod(className: String, name: String): String? {
        return try {
            if (methodResolveM != null) {
                methodResolveM!!.invoke(null, className, name) as? String
            } else if (finalMethod != null) {
                finalMethod!!.getMethod("resolve", String::class.java, String::class.java).invoke(null, className, name) as? String
            } else {
                null
            }
        } catch (_: Throwable) {
            null
        }
    }

    fun hasParameter(name: String): Boolean {
        return try {
            if (parameterHasM != null) {
                (parameterHasM!!.invoke(null, name) as? Boolean) == true
            } else if (finalParameter != null) {
                (finalParameter!!.getMethod("has", String::class.java).invoke(null, name) as? Boolean) == true
            } else {
                false
            }
        } catch (_: Throwable) {
            false
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun resolveParameter(name: String): Array<Class<*>>? {
        return try {
            if (parameterResolveM != null) {
                parameterResolveM!!.invoke(null, name) as? Array<Class<*>>
            } else if (finalParameter != null) {
                finalParameter!!.getMethod("resolve", String::class.java).invoke(null, name) as? Array<Class<*>>
            } else {
                null
            }
        } catch (_: Throwable) {
            null
        }
    }

    fun loadParameter() {
        try {
            if (loadParameterM != null) {
                loadParameterM!!.invoke(null)
            } else {
                clazz?.getMethod("loadParameter")?.invoke(null)
            }
        } catch (_: Throwable) {}
    }

    companion object {
        @JvmStatic
        fun getResolverClass(): Class<*>? {
            val clientType = ClientChecker.ClientType.fromPackage(Utils.pkgName) ?: return null
            return clientType.resolverClass
        }
    }
}
