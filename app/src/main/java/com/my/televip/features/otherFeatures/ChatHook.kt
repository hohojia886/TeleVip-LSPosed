package com.my.televip.features.otherFeatures

import android.content.Context
import android.text.InputType
import android.widget.EditText
import android.widget.LinearLayout
import com.my.televip.Class.ClassLoad
import com.my.televip.Class.ClassNames
import com.my.televip.ClientChecker
import com.my.televip.base.AbstractMethodHook
import com.my.televip.hooks.HMethod
import com.my.televip.language.Keys
import com.my.televip.language.Translator
import com.my.televip.logging.Logger
import com.my.televip.obfuscate.AutomationResolver
import com.my.televip.virtuals.ActionBar.ActionBarMenuItem
import com.my.televip.virtuals.ActionBar.AlertDialog
import com.my.televip.virtuals.Theme
import com.my.televip.virtuals.ui.ChatActivity
import de.robv.android.xposed.XposedHelpers

object ChatHook {
    private var initialized = false

    @JvmStatic
    fun init(context: Context, className: String) {
        if (initialized || ClientChecker.check(ClientChecker.ClientType.Nagram) || ClientChecker.check(ClientChecker.ClientType.TelegramPlus)) return

        val clazz = ClassLoad.getClass(className)
        if (clazz == null) FeatureStateManager.reset(context)
        try {
            initialized = true
            val chatActivityClass = ClassLoad.getClass(ClassNames.CHAT_ACTIVITY)
            if (chatActivityClass != null) {
                HMethod.hookMethod(
                    chatActivityClass,
                    AutomationResolver.resolve("ChatActivity", "createView", AutomationResolver.ResolverType.Method),
                    *AutomationResolver.merge(
                        AutomationResolver.resolveObject("createView", arrayOf<Class<*>>(Context::class.java)),
                        object : AbstractMethodHook() {
                            override fun afterMethod(param: MethodHookParam) {
                                try {
                                    val chatActivity = ChatActivity(param.thisObject)
                                    val headerItem = chatActivity.headerItem
                                    if (headerItem.actionBarMenuItem != null) {
                                        val drawableClass = ClassLoad.getClass(ClassNames.DRAWABLE)
                                        var drawableResource = XposedHelpers.getStaticIntField(drawableClass, "msg_go_up")
                                        if (!ClientChecker.check(ClientChecker.ClientType.iMe) && !ClientChecker.check(ClientChecker.ClientType.iMeWeb) && !ClientChecker.check(ClientChecker.ClientType.TelegramPlus) && !ClientChecker.check(ClientChecker.ClientType.XPlus) && !ClientChecker.check(ClientChecker.ClientType.forkgram) && !ClientChecker.check(ClientChecker.ClientType.forkgramBeta)) {
                                            headerItem.lazilyAddSubItem(8353847, drawableResource, Translator.get(Keys.ToTheBeginning))
                                        }
                                        drawableResource = XposedHelpers.getStaticIntField(drawableClass, "player_new_order")
                                        headerItem.lazilyAddSubItem(8353848, drawableResource, Translator.get(Keys.ToTheMessage))
                                    }
                                } catch (t: Throwable) {
                                    Logger.e(t)
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
                            try {
                                val id = param.args[0] as Int
                                val thisClass = XposedHelpers.getObjectField(param.thisObject, AutomationResolver.resolve("ChatActivity", "this$0", AutomationResolver.ResolverType.Field))
                                val chat = ChatActivity(thisClass)

                                if (id == 8353847) {
                                    chat.scrollToMessageId(1, 0, true, 0, true, 0)
                                } else if (id == 8353848) {
                                    val dialog = AlertDialog(context)
                                    dialog.setTitle(Translator.get(Keys.InputMessageId))

                                    val input = EditText(context)
                                    input.inputType = InputType.TYPE_CLASS_NUMBER
                                    if (Theme.isLight()) {
                                        input.setTextColor(-0x1000000)
                                        input.setHintTextColor(-0xbd3bd9)
                                    } else {
                                        input.setTextColor(-0x1)
                                        input.setHintTextColor(-0x424243)
                                    }
                                    input.textSize = 18f
                                    input.setPadding(20, 20, 20, 20)

                                    val params = LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT
                                    )
                                    params.setMargins(20, 20, 20, 20)
                                    input.layoutParams = params

                                    val layout = LinearLayout(context)
                                    layout.orientation = LinearLayout.VERTICAL
                                    layout.addView(input)

                                    dialog.setView(layout)
                                    dialog.setPositiveButton(Translator.get(Keys.Done), AlertDialog.click {
                                        val text = input.text.toString().trim()
                                        if (text.isNotEmpty()) {
                                            val msgId = text.toInt()
                                            chat.scrollToMessageId(msgId, 0, true, 0, true, 0)
                                        }
                                    })
                                    dialog.show()
                                }
                            } catch (t: Throwable) {
                                Logger.e(t)
                            }
                        }
                    }
                )
            }
        } catch (t: Throwable) {
            FeatureStateManager.reset(context)
        }
    }
}
