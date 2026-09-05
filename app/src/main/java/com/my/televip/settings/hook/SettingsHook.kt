package com.my.televip.settings.hook

import android.view.View
import android.widget.ImageView
import com.my.televip.Class.ClassLoad
import com.my.televip.Class.ClassNames
import com.my.televip.ClientChecker
import com.my.televip.Drawable.GhostDrawable
import com.my.televip.base.AbstractMethodHook
import com.my.televip.hooks.HMethod
import com.my.televip.language.Keys
import com.my.televip.language.Translator
import com.my.televip.logging.Logger
import com.my.televip.obfuscate.AutomationResolver
import com.my.televip.settings.controller.SettingsController
import com.my.televip.virtuals.SettingsIconResolver
import com.my.televip.utils.Utils
import com.my.televip.virtuals.Adapters.DrawerLayoutAdapter
import com.my.televip.virtuals.ui.Components.UItem
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import java.lang.reflect.Constructor
import java.lang.reflect.Method
import java.util.Arrays

class SettingsHook {

    private var itemConstructor: Constructor<*>? = null

    fun newSettings(settingsActivityClass: Class<*>, settingCellFactoryClass: Class<*>, settingsController: SettingsController) {
        try {
            val ghostDrawable = GhostDrawable()

            val settingCellClass = ClassLoad.getClass(ClassNames.SETTINGS_ACTIVITY_SETTING_CELL)
            if (settingCellClass != null) {
                HMethod.hookMethod(
                    settingCellClass,
                    AutomationResolver.resolve("SettingsActivity\$SettingCell", "set", AutomationResolver.ResolverType.Method),
                    *AutomationResolver.merge(
                        AutomationResolver.resolveObject("set", arrayOf<Class<*>>(Int::class.javaPrimitiveType!!, Int::class.javaPrimitiveType!!, Int::class.javaPrimitiveType!!, CharSequence::class.java, CharSequence::class.java, CharSequence::class.java)),
                        object : AbstractMethodHook() {
                            override fun afterMethod(param: MethodHookParam) {
                                val id = param.args[2] as Int
                                if (id == 8353847) {
                                    val iconView = XposedHelpers.getObjectField(
                                        param.thisObject,
                                        AutomationResolver.resolve("SettingsActivity\$SettingCell", "iconView", AutomationResolver.ResolverType.Field)
                                    ) as? ImageView
                                    iconView?.setImageDrawable(ghostDrawable)
                                }
                            }
                        }
                    )
                )
            }

            val universalAdapterClass = ClassLoad.getClass(ClassNames.UNIVERSAL_ADAPTER)
            if (universalAdapterClass != null) {
                HMethod.hookMethod(
                    settingsActivityClass,
                    AutomationResolver.resolve("SettingsActivity", "fillItems", AutomationResolver.ResolverType.Method),
                    *AutomationResolver.merge(
                        AutomationResolver.resolveObject("fillItems", arrayOf<Class<*>>(ArrayList::class.java, universalAdapterClass)),
                        object : AbstractMethodHook() {
                            @Suppress("UNCHECKED_CAST")
                            override fun afterMethod(param: MethodHookParam) {
                                val arrayList = param.args[0] as? ArrayList<Any>
                                if (arrayList != null) {
                                    val color1 = -0xb8909
                                    val color2 = -0x20aaaa
                                    val uItem = XposedHelpers.callStaticMethod(
                                        settingCellFactoryClass,
                                        AutomationResolver.resolve("SettingsActivity\$SettingCell\$Factory", "of", AutomationResolver.ResolverType.Method),
                                        8353847,
                                        color1,
                                        color2,
                                        8353847,
                                        Translator.get(Keys.GhostMode),
                                        Translator.get(Keys.ByMustafa)
                                    )
                                    for (i in arrayList.indices) {
                                        val item = UItem(arrayList[i])
                                        if (item.getText() != null && item.getSubtext() != null) {
                                            arrayList.add(i, uItem)
                                            break
                                        }
                                    }
                                }
                            }
                        }
                    )
                )
            }

            val uItemClass = ClassLoad.getClass(ClassNames.UITEM)
            if (uItemClass != null) {
                HMethod.hookMethod(
                    settingsActivityClass,
                    AutomationResolver.resolve("SettingsActivity", "onClick", AutomationResolver.ResolverType.Method),
                    *AutomationResolver.merge(
                        AutomationResolver.resolveObject("onClick", arrayOf<Class<*>>(uItemClass, View::class.java, Int::class.javaPrimitiveType!!, Float::class.javaPrimitiveType!!, Float::class.javaPrimitiveType!!)),
                        object : AbstractMethodHook() {
                            override fun afterMethod(param: MethodHookParam) {
                                val uItem = UItem(param.args[0])
                                if (uItem.getUItem() != null && uItem.getID() == 8353847) {
                                    settingsController.openView()
                                }
                            }
                        }
                    )
                )
            }
        } catch (t: Throwable) {
            Logger.e(t)
        }
    }

    fun oldSettings(settingsController: SettingsController) {
        val itemClass = XposedHelpers.findClassIfExists(
            AutomationResolver.resolve("org.telegram.ui.Adapters.DrawerLayoutAdapter\$Item"),
            Utils.classLoader
        )

        if (itemClass != null) {
            val drawerAdapterClass = ClassLoad.getClass(ClassNames.DRAWER_LAYOUT_ADAPTER)
            if (drawerAdapterClass != null) {
                HMethod.hookMethod(
                    drawerAdapterClass,
                    AutomationResolver.resolve("DrawerLayoutAdapter", "resetItems", AutomationResolver.ResolverType.Method),
                    object : AbstractMethodHook() {
                        @Suppress("UNCHECKED_CAST")
                        override fun afterMethod(param: MethodHookParam) {
                            val drawerLayoutAdapter = DrawerLayoutAdapter(param.thisObject)
                            val items = drawerLayoutAdapter.getItems()
                            if (itemConstructor == null) {
                                val paramTypes = AutomationResolver.resolveObject("item", arrayOf<Class<*>>(Int::class.javaPrimitiveType!!, CharSequence::class.java, Int::class.javaPrimitiveType!!))
                                itemConstructor = itemClass.getDeclaredConstructor(*paramTypes)
                                itemConstructor?.isAccessible = true
                            }
                            val newItem = itemConstructor?.newInstance(8353847, Translator.get(Keys.GhostMode), SettingsIconResolver.getIconSettings())
                            if (items is ArrayList<*>) {
                                val typedItems = items as ArrayList<Any>
                                if (newItem != null) {
                                    typedItems.add(newItem)
                                }
                            }
                        }
                    }
                )
            }

            val onCreateHook = object : AbstractMethodHook() {
                override fun afterMethod(param: MethodHookParam) {
                    val launch = param.thisObject
                    val drawerLayoutAdapter = XposedHelpers.getObjectField(
                        launch,
                        AutomationResolver.resolve("LaunchActivity", "drawerLayoutAdapter", AutomationResolver.ResolverType.Field)
                    )
                    if (drawerLayoutAdapter != null) {
                        val args = param.args[1]
                        val id = XposedHelpers.callMethod(
                            drawerLayoutAdapter,
                            AutomationResolver.resolve("DrawerLayoutAdapter", "getId", AutomationResolver.ResolverType.Method),
                            args
                        ) as Int
                        if (id == 8353847) {
                            val drawerLayoutContainer = XposedHelpers.getObjectField(
                                launch,
                                AutomationResolver.resolve("LaunchActivity", "drawerLayoutContainer", AutomationResolver.ResolverType.Field)
                            )
                            if (drawerLayoutContainer != null) {
                                if (!ClientChecker.check(ClientChecker.ClientType.ForkgramClassic)) {
                                    XposedHelpers.callMethod(drawerLayoutContainer, AutomationResolver.resolve("DrawerLayoutContainer", "closeDrawer", AutomationResolver.ResolverType.Method))
                                } else {
                                    XposedHelpers.callMethod(drawerLayoutContainer, AutomationResolver.resolve("DrawerLayoutContainer", "closeDrawer", AutomationResolver.ResolverType.Method), true)
                                }
                            }
                            settingsController.openView()
                        }
                    }
                }
            }

            val launchActivityClass = ClassLoad.getClass(ClassNames.LAUNCH_ACTIVITY)
            if (launchActivityClass != null) {
                var onCreateMethod: Method? = null
                val expectedParams = AutomationResolver.resolveObject("onCreateMethod", arrayOf<Class<*>>(View::class.java, Int::class.javaPrimitiveType!!, Float::class.javaPrimitiveType!!, Float::class.javaPrimitiveType!!))
                for (method in launchActivityClass.declaredMethods) {
                    if (Arrays.equals(method.parameterTypes, expectedParams)) {
                        onCreateMethod = method
                        break
                    }
                }

                if (onCreateMethod == null) {
                    Logger.w("Failed to hook onCreateMethod! Reason: No method found, " + Utils.issue)
                    return
                }

                XposedBridge.hookMethod(onCreateMethod, onCreateHook)
            }
        }
    }
}
