package com.my.televip.obfuscate;


import com.my.televip.obfuscate.struct.CustomResolverOverlay;
import com.my.televip.obfuscate.struct.ResolverRegistry;

import de.robv.android.xposed.XC_MethodHook;


public class AutomationResolver {

    public static ResolverRegistry resolverRegistry = new ResolverRegistry(ResolverRegistry.getResolverClass());

    public static String resolve(String className)
    {
        if (CustomResolverOverlay.hasClassOverride(className)) {
            return CustomResolverOverlay.getClassOverride(className);
        }
        if (resolverRegistry.hasClass(className)) {
            return resolverRegistry.resolveClass(className);
        }

        return className;
    }

    public static Class<?>[] resolveObject(String name, Class<?>[] classes) {

        if (resolverRegistry.hasParameter(name)) {
            return resolverRegistry.resolveParameter(name);
        }

        return classes;
    }

    public static String resolve(String className, String name, ResolverType type) {

        if (type == ResolverType.Field) {
            if (CustomResolverOverlay.hasFieldOverride(className, name)) {
                return CustomResolverOverlay.getFieldOverride(className, name);
            }
            if (resolverRegistry.hasField(className, name)) {
                return resolverRegistry.resolveField(className, name);
            }
        }
        String nameMethod = resolverRegistry.resolveMethodName(className, name);
        if (type == ResolverType.Method) {
            if (CustomResolverOverlay.hasMethodOverride(className, nameMethod)) {
                return CustomResolverOverlay.getMethodOverride(className, nameMethod);
            }
            if (resolverRegistry.hasMethod(className, nameMethod)) {
                return resolverRegistry.resolveMethod(className, nameMethod);
            }
        }

        name = name.replace("storyEntitiesAllowed2", "storyEntitiesAllowed");
        name = name.replace("hasStories2", "hasStories");

        return name;
    }


    public static Object[] merge(Class<?>[] classes, XC_MethodHook hook)
    {
        if (classes != null) {
            Object[] result = new Object[classes.length + 1];
            System.arraycopy(classes, 0, result, 0, classes.length);
            result[classes.length] = hook;
            return result;
        }
        return null;
    }

    public enum ResolverType
    {
        Field,
        Method
    }
}
