package com.my.televip.obfuscate.struct;

import java.util.HashMap;
import java.util.Map;

public class CustomResolverOverlay {

    private static final Map<String, String> classMap = new HashMap<>();
    private static final Map<String, String> fieldMap = new HashMap<>();
    private static final Map<String, String> methodMap = new HashMap<>();

    public static void putClassOverride(String originalName, String resolvedName) {
        classMap.put(originalName, resolvedName);
    }

    public static void putFieldOverride(String className, String fieldName, String resolvedFieldName) {
        fieldMap.put(className + "#" + fieldName, resolvedFieldName);
    }

    public static void putMethodOverride(String className, String methodName, String resolvedMethodName) {
        methodMap.put(className + "#" + methodName, resolvedMethodName);
    }

    public static boolean hasClassOverride(String originalName) {
        return classMap.containsKey(originalName);
    }

    public static String getClassOverride(String originalName) {
        return classMap.get(originalName);
    }

    public static boolean hasFieldOverride(String className, String fieldName) {
        return fieldMap.containsKey(className + "#" + fieldName);
    }

    public static String getFieldOverride(String className, String fieldName) {
        return fieldMap.get(className + "#" + fieldName);
    }

    public static boolean hasMethodOverride(String className, String methodName) {
        return methodMap.containsKey(className + "#" + methodName);
    }

    public static String getMethodOverride(String className, String methodName) {
        return methodMap.get(className + "#" + methodName);
    }
}
