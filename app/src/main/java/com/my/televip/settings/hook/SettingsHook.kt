package com.my.televip.settings.hook

import android.view.View
import android.widget.ImageView
import com.my.televip.clazz.ClassLoad
import com.my.televip.clazz.ClassNames
import com.my.televip.clients.ClientManager
import com.my.televip.drawable.ArrowDrawable
import com.my.televip.base.BaseMethodHook
import com.my.televip.hooks.HMethod
import com.my.televip.language.Keys
import com.my.televip.language.Translator
import com.my.televip.logging.Logger
import com.my.televip.obfuscate.ArgsResolver
import com.my.televip.obfuscate.Obfuscate
import com.my.televip.settings.controller.SettingsController
import com.my.televip.utils.Utils
import com.my.televip.virtuals.Adapters.DrawerLayoutAdapter
import com.my.televip.virtuals.SettingsIconResolver
import com.my.televip.virtuals.ui.Components.UItem
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import java.lang.reflect.Constructor
import java.lang.reflect.Method
import java.util.ArrayList
import java.util.Arrays

class SettingsHook {

    private var itemConstructor: Constructor<*>? = null

    fun newSettings(
        settingsActivityClass: Class<*>,
        factoryClass: Class<*>,
        settingsController: SettingsController
    ) {
        try {
            val arrowDrawable = ArrowDrawable()

            val settingCellClass = ClassLoad.getClass(ClassNames.SETTINGS_ACTIVITY_SETTING_CELL)
            if (settingCellClass != null) {
                val setTypes: Array<Class<*>> = arrayOf(
                    Int::class.javaPrimitiveType!!,
                    Int::class.javaPrimitiveType!!,
                    Int::class.javaPrimitiveType!!,
                    CharSequence::class.java,
                    CharSequence::class.java,
                    CharSequence::class.java
                )
                val setMerged = ArgsResolver.merge("set", setTypes, object : BaseMethodHook() {
                    override fun afterMethod(param: MethodHookParam) {
                        val id = param.args[2] as Int
                        if (id == 8353847) {
                            val iconView = XposedHelpers.getObjectField(
                                param.thisObject,
                                Obfuscate.getFieldName("SettingsActivity\$SettingCell", "iconView")
                            ) as? ImageView
                            iconView?.setImageDrawable(arrowDrawable)
                        }
                    }
                })
                if (setMerged != null) {
                    XposedHelpers.findAndHookMethod(
                        settingCellClass,
                        Obfuscate.getMethodName("SettingsActivity\$SettingCell", "set"),
                        *setMerged
                    )
                }
            }

            val fillTypes: Array<Class<*>> = arrayOf(
                ArrayList::class.java,
                ClassLoad.getClass(ClassNames.UNIVERSAL_ADAPTER)!!
            )
            val fillMerged = ArgsResolver.merge("fillItems", fillTypes, object : BaseMethodHook() {
                override fun afterMethod(param: MethodHookParam) {
                    @Suppress("UNCHECKED_CAST")
                    val arrayList = param.args[0] as? ArrayList<Any> ?: return
                    val color1 = -0xb0911
                    val color2 = -0x20aaab

                    val uItem = XposedHelpers.callStaticMethod(
                        factoryClass,
                        Obfuscate.getMethodName("SettingsActivity\$SettingCell\$Factory", "of"),
                        8353847,
                        color1,
                        color2,
                        8353847,
                        Translator.get(Keys.GhostMode),
                        Translator.get(Keys.ByMustafa)
                    )

                    for (i in arrayList.indices) {
                        val item = UItem(arrayList[i])
                        if (item.text != null && item.subtext != null) {
                            arrayList.add(i, uItem)
                            break
                        }
                    }
                }
            })
            if (fillMerged != null) {
                XposedHelpers.findAndHookMethod(
                    settingsActivityClass,
                    Obfuscate.getMethodName("SettingsActivity", "fillItems"),
                    *fillMerged
                )
            }

            val uItemClass = ClassLoad.getClass(ClassNames.UITEM)
            if (uItemClass != null) {
                val clickTypes: Array<Class<*>> = arrayOf(
                    uItemClass,
                    View::class.java,
                    Int::class.javaPrimitiveType!!,
                    Float::class.javaPrimitiveType!!,
                    Float::class.javaPrimitiveType!!
                )
                val clickMerged = ArgsResolver.merge("onClick", clickTypes, object : BaseMethodHook() {
                    override fun afterMethod(param: MethodHookParam) {
                        val uItem = UItem(param.args[0])
                        if (uItem.uItem != null && uItem.id == 8353847) {
                            settingsController.openView()
                        }
                    }
                })
                if (clickMerged != null) {
                    XposedHelpers.findAndHookMethod(
                        settingsActivityClass,
                        Obfuscate.getMethodName("SettingsActivity", "onClick"),
                        *clickMerged
                    )
                }
            }
        } catch (t: Throwable) {
            Logger.e(t)
        }
    }

    fun oldSettings(settingsController: SettingsController) {
        val itemClass = XposedHelpers.findClassIfExists(
            Obfuscate.getClassName("org.telegram.ui.Adapters.DrawerLayoutAdapter\$Item"),
            Utils.classLoader
        )

        if (itemClass != null) {
            val drawerClass = ClassLoad.getClass(ClassNames.DRAWER_LAYOUT_ADAPTER)
            if (drawerClass != null) {
                HMethod.hookMethod(
                    drawerClass,
                    Obfuscate.getMethodName("DrawerLayoutAdapter", "resetItems"),
                    object : BaseMethodHook() {
                        override fun afterMethod(param: MethodHookParam) {
                            val drawerLayoutAdapter = DrawerLayoutAdapter(param.thisObject)
                            val items = drawerLayoutAdapter.items

                            if (itemConstructor == null) {
                                val itemArgTypes = ArgsResolver.resolveObject(
                                    "item",
                                    arrayOf(Int::class.javaPrimitiveType!!, CharSequence::class.java, Int::class.javaPrimitiveType!!)
                                )
                                itemConstructor = itemClass.getDeclaredConstructor(*itemArgTypes)
                                itemConstructor?.isAccessible = true
                            }

                            val newItem = itemConstructor?.newInstance(
                                8353847,
                                Translator.get(Keys.GhostMode),
                                SettingsIconResolver.getIconSettings()
                            )

                            if (items is ArrayList<*>) {
                                @Suppress("UNCHECKED_CAST")
                                val typedItems = items as ArrayList<Any>
                                if (newItem != null) {
                                    typedItems.add(newItem)
                                }
                            }
                        }
                    }
                )
            }

            val onCreateHook = object : BaseMethodHook() {
                override fun afterMethod(param: MethodHookParam) {
                    val launch = param.thisObject
                    val drawerLayoutAdapter = XposedHelpers.getObjectField(
                        launch,
                        Obfuscate.getFieldName("LaunchActivity", "drawerLayoutAdapter")
                    )
                    if (drawerLayoutAdapter != null) {
                        val args = param.args[1]
                        val id = XposedHelpers.callMethod(
                            drawerLayoutAdapter,
                            Obfuscate.getMethodName("DrawerLayoutAdapter", "getId"),
                            args
                        ) as Int

                        if (id == 8353847) {
                            val drawerLayoutContainer = XposedHelpers.getObjectField(
                                launch,
                                Obfuscate.getFieldName("LaunchActivity", "drawerLayoutContainer")
                            )
                            if (drawerLayoutContainer != null) {
                                if (!ClientManager.`is`(ClientManager.Client.ForkgramClassic)) {
                                    XposedHelpers.callMethod(
                                        drawerLayoutContainer,
                                        Obfuscate.getMethodName("DrawerLayoutContainer", "closeDrawer")
                                    )
                                } else {
                                    XposedHelpers.callMethod(
                                        drawerLayoutContainer,
                                        Obfuscate.getMethodName("DrawerLayoutContainer", "closeDrawer"),
                                        true
                                    )
                                }
                            }
                            settingsController.openView()
                        }
                    }
                }
            }

            val launchClass = ClassLoad.getClass(ClassNames.LAUNCH_ACTIVITY)
            if (launchClass != null) {
                var onCreateMethod: Method? = null
                val targetParams = ArgsResolver.resolveObject(
                    "onCreateMethod",
                    arrayOf(
                        View::class.java,
                        Int::class.javaPrimitiveType!!,
                        Float::class.javaPrimitiveType!!,
                        Float::class.javaPrimitiveType!!
                    )
                )

                for (method in launchClass.declaredMethods) {
                    if (Arrays.equals(method.parameterTypes, targetParams)) {
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
