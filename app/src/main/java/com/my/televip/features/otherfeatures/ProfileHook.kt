package com.my.televip.features.otherfeatures

import com.my.televip.clazz.ClassLoad
import com.my.televip.clazz.ClassNames
import com.my.televip.clients.ClientManager
import com.my.televip.base.BaseMethodHook
import com.my.televip.hooks.HMethod
import com.my.televip.language.Keys
import com.my.televip.language.Translator
import com.my.televip.obfuscate.ArgsResolver
import com.my.televip.obfuscate.Obfuscate
import com.my.televip.utils.IdDateEstimator
import com.my.televip.utils.Utils
import com.my.televip.virtuals.ActionBar.AlertDialog
import com.my.televip.virtuals.ui.ProfileActivity
import de.robv.android.xposed.XposedHelpers

object ProfileHook {

    private var initialized = false

    @JvmStatic
    fun init(className: String?) {
        if (initialized || className == null) return

        val clazz = ClassLoad.getClass(className)
        if (clazz == null) FeatureStateManager.reset()

        initialized = true

        val createTypes: Array<Class<*>> = arrayOf(Boolean::class.javaPrimitiveType!!)
        val createMerged = ArgsResolver.merge("createActionBarMenu", createTypes, object : BaseMethodHook() {
            override fun afterMethod(param: MethodHookParam) {
                val profileActivity = ProfileActivity(param.thisObject)
                if (getUserID(profileActivity) > 1) {
                    val otherItem = profileActivity.otherItem
                    if (otherItem.actionBarMenuItem != null) {
                        var drawableResource = 0x7f0806d3
                        if (!ClientManager.`is`(ClientManager.Client.Nagram) && !ClientManager.`is`(ClientManager.Client.Momogram)) {
                            val drawableClass = ClassLoad.getClass(ClassNames.DRAWABLE)
                            drawableResource = XposedHelpers.getStaticIntField(drawableClass, "msg_filled_menu_users")
                        }
                        otherItem.addSubItem(8353847, drawableResource, Translator.get(Keys.ApproximateCreationDate))
                    }
                }
            }
        })
        if (createMerged != null) {
            val profClass = ClassLoad.getClass(ClassNames.PROFILE_ACTIVITY)
            if (profClass != null) {
                XposedHelpers.findAndHookMethod(
                    profClass,
                    Obfuscate.getMethodName("ProfileActivity", "createActionBarMenu"),
                    *createMerged
                )
            }
        }

        if (clazz != null) {
            XposedHelpers.findAndHookMethod(clazz, "onItemClick", Int::class.javaPrimitiveType!!, object : BaseMethodHook() {
                override fun afterMethod(param: MethodHookParam) {
                    val id = param.args[0] as Int
                    if (id == 8353847) {
                        val thisClass = XposedHelpers.getObjectField(param.thisObject, Obfuscate.getFieldName("ProfileActivity", "this$0"))
                        val profile = ProfileActivity(thisClass)

                        val alertDialog = AlertDialog(Utils.getCurrentActivity())
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
            })
        }
    }

    private fun getUserID(profile: ProfileActivity): Long {
        return if (profile.userId > 1) {
            profile.userId
        } else {
            0
        }
    }
}
