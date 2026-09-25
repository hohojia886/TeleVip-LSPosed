package com.my.televip.features.otherfeatures

import android.content.Context
import android.text.InputType
import android.widget.EditText
import android.widget.LinearLayout
import com.my.televip.clazz.ClassLoad
import com.my.televip.clazz.ClassNames
import com.my.televip.clients.ClientManager
import com.my.televip.base.BaseMethodHook
import com.my.televip.language.Keys
import com.my.televip.language.Translator
import com.my.televip.logging.Logger
import com.my.televip.obfuscate.ArgsResolver
import com.my.televip.obfuscate.Obfuscate
import com.my.televip.utils.Utils
import com.my.televip.virtuals.ActionBar.AlertDialog
import com.my.televip.virtuals.ActionBar.Theme
import com.my.televip.virtuals.ui.ChatActivity
import de.robv.android.xposed.XposedHelpers

object ChatHook {

    private var initialized = false

    @JvmStatic
    fun init(className: String?) {
        if (initialized || className == null ||
            ClientManager.`is`(ClientManager.Client.Nagram) ||
            ClientManager.`is`(ClientManager.Client.TelegramPlus)
        ) return

        val clazz = ClassLoad.getClass(className)
        if (clazz == null) FeatureStateManager.reset()

        try {
            initialized = true
            val createTypes: Array<Class<*>> = arrayOf(Context::class.java)
            val createMerged = ArgsResolver.merge("createView", createTypes, object : BaseMethodHook() {
                override fun afterMethod(param: MethodHookParam) {
                    try {
                        val chatActivity = ChatActivity(param.thisObject)
                        val headerItem = chatActivity.headerItem
                        if (headerItem.actionBarMenuItem != null) {
                            val drawableClass = ClassLoad.getClass(ClassNames.DRAWABLE)
                            var drawableResource = XposedHelpers.getStaticIntField(drawableClass, "msg_go_up")

                            if (!ClientManager.`is`(ClientManager.Client.iMe) &&
                                !ClientManager.`is`(ClientManager.Client.iMeWeb) &&
                                !ClientManager.`is`(ClientManager.Client.TelegramPlus) &&
                                !ClientManager.`is`(ClientManager.Client.XPlus) &&
                                !ClientManager.`is`(ClientManager.Client.forkgram) &&
                                !ClientManager.`is`(ClientManager.Client.forkgramBeta)
                            ) {
                                headerItem.lazilyAddSubItem(8353847, drawableResource, Translator.get(Keys.ToTheBeginning))
                            }
                            drawableResource = XposedHelpers.getStaticIntField(drawableClass, "player_new_order")
                            headerItem.lazilyAddSubItem(8353848, drawableResource, Translator.get(Keys.ToTheMessage))
                        }
                    } catch (t: Throwable) {
                        Logger.e(t)
                    }
                }
            })
            if (createMerged != null) {
                val chatClass = ClassLoad.getClass(ClassNames.CHAT_ACTIVITY)
                if (chatClass != null) {
                    XposedHelpers.findAndHookMethod(
                        chatClass,
                        Obfuscate.getMethodName("ChatActivity", "createView"),
                        *createMerged
                    )
                }
            }

            if (clazz != null) {
                XposedHelpers.findAndHookMethod(clazz, "onItemClick", Int::class.javaPrimitiveType!!, object : BaseMethodHook() {
                    override fun afterMethod(param: MethodHookParam) {
                        try {
                            val id = param.args[0] as Int
                            val thisClass = XposedHelpers.getObjectField(param.thisObject, Obfuscate.getFieldName("ChatActivity", "this$0"))
                            val chat = ChatActivity(thisClass)

                            if (id == 8353847) {
                                chat.scrollToMessageId(1, 0, true, 0, true, 0)
                            } else if (id == 8353848) {
                                val dialog = AlertDialog(Utils.getCurrentActivity())
                                dialog.setTitle(Translator.get(Keys.InputMessageId))

                                val input = EditText(Utils.getCurrentActivity())
                                input.inputType = InputType.TYPE_CLASS_NUMBER
                                if (Theme.isLight()) {
                                    input.setTextColor(-0x1000000)
                                    input.setHintTextColor(-0xbdbebe)
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

                                val layout = LinearLayout(Utils.getCurrentActivity())
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
                })
            }
        } catch (t: Throwable) {
            FeatureStateManager.reset()
        }
    }
}
