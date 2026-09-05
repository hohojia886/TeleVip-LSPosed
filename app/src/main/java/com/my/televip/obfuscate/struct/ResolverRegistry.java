package com.my.televip.obfuscate.struct;

import com.my.televip.ClientChecker;
import com.my.televip.utils.Utils;

import java.lang.reflect.Method;

public class ResolverRegistry {

    Class<?> finalField;
    Class<?> finalMethod;
    Class<?> finalParameter;
    Class<?> finalClass;
    Class<?> clazz;

    private Method resolveMethodNameM;
    private Method classHasM;
    private Method classResolveM;
    private Method fieldHasM;
    private Method fieldResolveM;
    private Method methodHasM;
    private Method methodResolveM;
    private Method parameterHasM;
    private Method parameterResolveM;
    private Method loadParameterM;

    public ResolverRegistry(Class<?> clazz){
        this.clazz = clazz;
        if (clazz != null) {
            try {
                Class<?> fieldResolverClass = null;
                Class<?> methodResolverClass = null;
                Class<?> parameterResolverClass = null;
                Class<?> classResolverClass = null;
                for (Class<?> inner : clazz.getDeclaredClasses()) {
                    if (inner.getSimpleName().equals("FieldResolver")) fieldResolverClass = inner;
                    if (inner.getSimpleName().equals("MethodResolver")) methodResolverClass = inner;
                    if (inner.getSimpleName().equals("ParameterResolver")) parameterResolverClass = inner;
                    if (inner.getSimpleName().equals("ClassResolver")) classResolverClass = inner;
                }

                finalField = fieldResolverClass;
                finalMethod = methodResolverClass;
                finalParameter = parameterResolverClass;
                finalClass = classResolverClass;

                try { resolveMethodNameM = clazz.getMethod("resolveMethodName", String.class, String.class); } catch (Throwable ignored) {}
                try { loadParameterM = clazz.getMethod("loadParameter"); } catch (Throwable ignored) {}

                if (finalClass != null) {
                    try { classHasM = finalClass.getMethod("has", String.class); } catch (Throwable ignored) {}
                    try { classResolveM = finalClass.getMethod("resolve", String.class); } catch (Throwable ignored) {}
                }
                if (finalField != null) {
                    try { fieldHasM = finalField.getMethod("has", String.class, String.class); } catch (Throwable ignored) {}
                    try { fieldResolveM = finalField.getMethod("resolve", String.class, String.class); } catch (Throwable ignored) {}
                }
                if (finalMethod != null) {
                    try { methodHasM = finalMethod.getMethod("has", String.class, String.class); } catch (Throwable ignored) {}
                    try { methodResolveM = finalMethod.getMethod("resolve", String.class, String.class); } catch (Throwable ignored) {}
                }
                if (finalParameter != null) {
                    try { parameterHasM = finalParameter.getMethod("has", String.class); } catch (Throwable ignored) {}
                    try { parameterResolveM = finalParameter.getMethod("resolve", String.class); } catch (Throwable ignored) {}
                }

            } catch (Throwable ignored) {}
        }
    }

    public String resolveMethodName(String className, String name) {
        try {
            if (resolveMethodNameM != null) {
                return (String) resolveMethodNameM.invoke(null, className, name);
            }
            if (clazz != null) {
                return (String) clazz.getMethod("resolveMethodName", String.class, String.class).invoke(null, className, name);
            }
        } catch (Throwable e){
            return name;
        }
        return name;
    }

    public boolean hasClass(String className){
        try {
            if (classHasM != null) {
                Boolean res = (Boolean) classHasM.invoke(null, className);
                return res != null && res;
            }
            if (finalClass != null) {
                Boolean res = (Boolean) finalClass.getMethod("has", String.class).invoke(null, className);
                return res != null && res;
            }
        } catch (Throwable e){
            return false;
        }
        return false;
    }

    public String resolveClass(String className){
        try {
            if (classResolveM != null) {
                return (String) classResolveM.invoke(null, className);
            }
            if (finalClass != null) {
                return (String) finalClass.getMethod("resolve", String.class).invoke(null, className);
            }
        } catch (Throwable e){
            return null;
        }
        return null;
    }

    public boolean hasField(String className, String name){
        try {
            if (fieldHasM != null) {
                Boolean res = (Boolean) fieldHasM.invoke(null, className, name);
                return res != null && res;
            }
            if (finalField != null) {
                Boolean res = (Boolean) finalField.getMethod("has", String.class, String.class).invoke(null, className, name);
                return res != null && res;
            }
        } catch (Throwable e){
            return false;
        }
        return false;
    }

    public String resolveField(String className, String name){
        try {
            if (fieldResolveM != null) {
                return (String) fieldResolveM.invoke(null, className, name);
            }
            if (finalField != null) {
                return (String) finalField.getMethod("resolve", String.class, String.class).invoke(null, className, name);
            }
        } catch (Throwable e){
            return null;
        }
        return null;
    }

    public boolean hasMethod(String className, String name){
        try {
            if (methodHasM != null) {
                Boolean res = (Boolean) methodHasM.invoke(null, className, name);
                return res != null && res;
            }
            if (finalMethod != null) {
                Boolean res = (Boolean) finalMethod.getMethod("has", String.class, String.class).invoke(null, className, name);
                return res != null && res;
            }
        } catch (Throwable e){
            return false;
        }
        return false;
    }

    public String resolveMethod(String className, String name){
        try {
            if (methodResolveM != null) {
                return (String) methodResolveM.invoke(null, className, name);
            }
            if (finalMethod != null) {
                return (String) finalMethod.getMethod("resolve", String.class, String.class).invoke(null, className, name);
            }
        } catch (Throwable e){
            return null;
        }
        return null;
    }

    public boolean hasParameter(String name){
        try {
            if (parameterHasM != null) {
                Boolean res = (Boolean) parameterHasM.invoke(null, name);
                return res != null && res;
            }
            if (finalParameter != null) {
                Boolean res = (Boolean) finalParameter.getMethod("has", String.class).invoke(null, name);
                return res != null && res;
            }
        } catch (Throwable e){
            return false;
        }
        return false;
    }

    public Class<?>[] resolveParameter(String name){
        try {
            if (parameterResolveM != null) {
                return (Class<?>[]) parameterResolveM.invoke(null, name);
            }
            if (finalParameter != null) {
                return (Class<?>[]) finalParameter.getMethod("resolve", String.class).invoke(null, name);
            }
        } catch (Throwable e){
            return null;
        }
        return null;
    }

    public void loadParameter(){
        try {
            if (loadParameterM != null) {
                loadParameterM.invoke(null);
            } else if (clazz != null) {
                clazz.getMethod("loadParameter").invoke(null);
            }
        } catch (Throwable ignored){}
    }

    public static Class<?> getResolverClass() {
        ClientChecker.ClientType clientType = ClientChecker.ClientType.fromPackage(Utils.pkgName);
        if (clientType == null) return null;
        return clientType.getResolverClass();
    }
}