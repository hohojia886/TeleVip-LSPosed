package com.my.televip.features.otherFeatures

import android.content.Context
import com.my.televip.Class.ClassLoad
import com.my.televip.Class.ClassNames
import com.my.televip.ClientChecker
import com.my.televip.base.AbstractMethodHook
import com.my.televip.hooks.HMethod
import com.my.televip.language.Keys
import com.my.televip.language.Translator
import com.my.televip.obfuscate.AutomationResolver
import com.my.televip.utils.IdDateEstimator
import com.my.televip.virtuals.ActionBar.AlertDialog
import com.my.televip.virtuals.ui.ProfileActivity
import de.robv.android.xposed.XposedHelpers

object ProfileHook {
    private var initialized = false

    @JvmStatic
    fun init(context: Context, className: String?) {
        if (initialized || className == null) return

        val clazz = ClassLoad.getClass(className)
        if (clazz == null) FeatureStateManager.reset(context)

        initialized = true

        val profileActivityClass = ClassLoad.getClass(ClassNames.PROFILE_ACTIVITY)
        if (profileActivityClass != null) {
            HMethod.hookMethod(
                profileActivityClass,
                AutomationResolver.resolve("ProfileActivity", "createActionBarMenu", AutomationResolver.ResolverType.Method),
                *AutomationResolver.merge(
                    AutomationResolver.resolveObject("createActionBarMenu", arrayOf<Class<*>>(Boolean::class.javaPrimitiveType!!)),
                    object : AbstractMethodHook() {
                        override fun afterMethod(param: MethodHookParam) {
                            val profileActivity = ProfileActivity(param.thisObject)
                            if (getUserID(profileActivity) > 1) {
                                val otherItem = profileActivity.otherItem
                                if (otherItem.actionBarMenuItem != null) {
                                    var drawableResource = 0x7f0806d3
                                    if (!ClientChecker.check(ClientChecker.ClientType.Nagram) && !ClientChecker.check(ClientChecker.ClientType.Momogram)) {
                                        val drawableClass = ClassLoad.getClass(ClassNames.DRAWABLE)
                                        drawableResource = XposedHelpers.getStaticIntField(drawableClass, "msg_filled_menu_users")
                                    }
                                    otherItem.addSubItem(8353847, drawableResource, Translator.get(Keys.ApproximateCreationDate))
                                }
                            }
                        }
                    }
                )
            )
        }

        if (clazz != null) {
            HMethod.hookMethod(
                clazz,
                "onItemClick",
                Int::class.javaPrimitiveType!!,
                object : AbstractMethodHook() {
                    override fun afterMethod(param: MethodHookParam) {
                        val id = param.args[0] as Int
                        if (id == 8353847) {
                            val thisClass = XposedHelpers.getObjectField(param.thisObject, AutomationResolver.resolve("ProfileActivity", "this$0", AutomationResolver.ResolverType.Field))
                            val profile = ProfileActivity(thisClass)

                            val alertDialog = AlertDialog(context)
                            alertDialog.setTitle(Translator.get(Keys.TeleVip))
                            alertDialog.setMessage(
                                "\n" +
                                        Translator.get(Keys.ApproximateCreationDate) + " : " +
                                        IdDateEstimator.getYearAndMethod(getUserID(profile)) + "\n\n" +
                                        Translator.get(Keys.Age) + " : " +
                                        IdDateEstimator.getAge(getUserID(profile)) + "\n\n" +
                                        Translator.get(Keys.ApproximateCreationDateNotice)
                            )
                            alertDialog.setPositiveButton(Translator.get(Keys.Done), null)
                            alertDialog.show()
                        }
                    }
                }
            )
        }
    }

    private fun getUserID(profile: ProfileActivity): Long {
        return if (profile.userId > 1) {
            profile.userId
        } else {
            0L
        }
    }
}
