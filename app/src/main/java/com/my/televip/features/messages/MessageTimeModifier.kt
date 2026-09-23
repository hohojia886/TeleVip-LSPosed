package com.my.televip.features.messages

import android.graphics.Color
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import com.my.televip.Class.ClassLoad
import com.my.televip.Class.ClassNames
import com.my.televip.Configs.ConfigManager
import com.my.televip.base.BaseMethodHook
import com.my.televip.hooks.HMethod
import com.my.televip.language.Keys
import com.my.televip.language.Translator
import com.my.televip.logging.Logger
import com.my.televip.obfuscate.ArgsResolver
import com.my.televip.obfuscate.Obfuscate
import com.my.televip.virtuals.ActionBar.Theme
import com.my.televip.virtuals.messenger.MessageObject
import com.my.televip.virtuals.tgnet.TLRPC
import com.my.televip.virtuals.ui.Cells.ChatMessageCell
import de.robv.android.xposed.XposedHelpers

object MessageTimeModifier {

    @JvmField
    var loaded: Boolean = false

    @JvmStatic
    fun init() {
        if (loaded) return
        loaded = true

        try {
            val cellClass = ClassLoad.getClass(ClassNames.CHAT_MESSAGE_CELL)
            if (cellClass != null) {
                val argTypes: Array<Class<*>> = arrayOf(ClassLoad.getClass(ClassNames.MESSAGE_OBJECT)!!)
                val merged = ArgsResolver.merge("measureTime", argTypes, object : BaseMethodHook() {
                    override fun afterMethod(param: MethodHookParam) {
                        try {
                            apply(param.thisObject, param.args[0])
                        } catch (e: Throwable) {
                            Logger.e(e)
                        }
                    }
                })
                if (merged != null) {
                    XposedHelpers.findAndHookMethod(
                        cellClass,
                        Obfuscate.getMethodName("ChatMessageCell", "measureTime"),
                        *merged
                    )
                }
            }
        } catch (e: Throwable) {
            Logger.e(e)
        }
    }

    private fun apply(cellObject: Any, messageObjectParam: Any) {
        val id = ConfigManager.showMessageId?.isEnable == true
        val deleted = ConfigManager.showDeletedMessages?.isEnable == true

        if (!id && !deleted) return

        try {
            val msg = MessageObject(messageObjectParam)
            val message = msg.messageOwner
            if (message.get_Message() == null) return

            var prefix: String? = null
            var redColor = false

            if (id && message.id != 0) {
                prefix = "ID ${message.id}"
            }

            if (deleted && (message.flags and ShowDeletedMessages.FLAG_DELETED) != 0) {
                prefix = Translator.get(Keys.Deleted)
                redColor = true
            }

            if (prefix != null) {
                prependTime(cellObject, prefix, redColor)
            }
        } catch (e: Throwable) {
            Logger.e(e)
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

    private fun prependTime(objectParam: Any, value: String, red: Boolean) {
        val cell = ChatMessageCell(objectParam)
        var time = convertToStringBuilder(cell.currentTimeString) ?: return

        val spannableStringBuilder = SpannableStringBuilder(value)
        if (red) {
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
            val ceil = Math.ceil(paint.measureText(spannableStringBuilder, 0, spannableStringBuilder.length).toDouble()).toInt()
            cell.timeTextWidth = ceil + cell.timeTextWidth
            cell.timeWidth = ceil + cell.timeWidth
        }
    }
}
