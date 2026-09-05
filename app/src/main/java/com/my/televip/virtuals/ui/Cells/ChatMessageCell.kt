package com.my.televip.virtuals.ui.Cells

import android.graphics.Color
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import com.my.televip.Class.ClassLoad
import com.my.televip.Class.ClassNames
import com.my.televip.Configs.ConfigManager
import com.my.televip.base.AbstractMethodHook
import com.my.televip.features.ShowDeletedMessages
import com.my.televip.hooks.HMethod
import com.my.televip.language.Keys
import com.my.televip.language.Translator
import com.my.televip.logging.Logger
import com.my.televip.obfuscate.AutomationResolver
import com.my.televip.virtuals.OfficialChatMessageCell
import com.my.televip.virtuals.Theme
import com.my.televip.virtuals.messenger.MessageObject
import de.robv.android.xposed.XposedHelpers
import kotlin.math.ceil

class ChatMessageCell(@JvmField val chatMessageCell: Any?) {

    fun getMessageObject(): MessageObject {
        return MessageObject(
            XposedHelpers.callMethod(
                chatMessageCell,
                AutomationResolver.resolve("ChatMessageCell", "getMessageObject", AutomationResolver.ResolverType.Method)
            )
        )
    }

    companion object {
        @JvmField var isEnable: Boolean = false

        @JvmStatic
        fun init() {
            try {
                if (!isEnable) {
                    isEnable = true
                    val chatMsgCellClass = ClassLoad.getClass(ClassNames.CHAT_MESSAGE_CELL)
                    if (chatMsgCellClass != null) {
                        val msgObjClass = ClassLoad.getClass(ClassNames.MESSAGE_OBJECT) ?: return
                        HMethod.hookMethod(
                            chatMsgCellClass,
                            AutomationResolver.resolve("ChatMessageCell", "measureTime", AutomationResolver.ResolverType.Method),
                            *AutomationResolver.merge(
                                AutomationResolver.resolveObject("measureTime", arrayOf<Class<*>>(msgObjClass)),
                                object : AbstractMethodHook() {
                                    override fun afterMethod(param: MethodHookParam) {
                                        val showDeleted = ConfigManager.showDeletedMessages.isEnable
                                        val showMessageId = ConfigManager.showMessageId.isEnable

                                        if (showDeleted || showMessageId) {
                                            try {
                                                val messageObject = MessageObject(param.args[0])
                                                if (messageObject.messageObject == null) return

                                                val owner = messageObject.getMessageOwner()
                                                if (showMessageId) {
                                                    if (owner.getID() != 0) {
                                                        val textId = "ID ${owner.getID()}"
                                                        setSpannableStringBuilderText(textId, param.thisObject, false)
                                                    }
                                                }

                                                val flags = owner.getFlags()
                                                if ((flags and ShowDeletedMessages.FLAG_DELETED) != 0 && showDeleted) {
                                                    setSpannableStringBuilderText(Translator.get(Keys.Deleted), param.thisObject, true)
                                                } else {
                                                    val paint = Theme.getTextPaint()
                                                    paint?.setShadowLayer(0f, 0f, 0f, Color.WHITE)
                                                }
                                            } catch (throwable: Throwable) {
                                                Logger.e(throwable)
                                            }
                                        }
                                    }
                                }
                            )
                        )
                    }
                }
            } catch (t: Throwable) {
                Logger.e(t)
            }
        }

        @JvmStatic
        fun convertToStringBuilder(charSequence: CharSequence?): SpannableStringBuilder? {
            return when (charSequence) {
                null -> null
                is SpannableStringBuilder -> charSequence
                else -> SpannableStringBuilder(charSequence)
            }
        }

        private fun setSpannableStringBuilderText(text: String, thisObject: Any, color: Boolean) {
            val cell = OfficialChatMessageCell(thisObject)
            val time = convertToStringBuilder(cell.getCurrentTimeString()) ?: return
            val spannableStringBuilder = SpannableStringBuilder(text)
            if (color) {
                spannableStringBuilder.setSpan(
                    ForegroundColorSpan(Color.rgb(255, 0, 0)),
                    0,
                    spannableStringBuilder.length,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
                )
            }
            spannableStringBuilder.append(" ")
            time.insert(0, spannableStringBuilder)
            cell.setCurrentTimeString(time)
            val paint = Theme.getTextPaint()
            if (paint != null) {
                val ceilVal = ceil(paint.measureText(spannableStringBuilder, 0, spannableStringBuilder.length).toDouble()).toInt()
                cell.setTimeTextWidth(ceilVal + cell.getTimeTextWidth())
                cell.setTimeWidth(ceilVal + cell.getTimeWidth())
            }
        }
    }
}
