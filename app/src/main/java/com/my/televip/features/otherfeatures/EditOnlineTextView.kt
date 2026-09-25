package com.my.televip.features.otherfeatures

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.widget.Toast
import com.my.televip.clazz.ClassLoad
import com.my.televip.clazz.ClassNames
import com.my.televip.configs.ConfigManager
import com.my.televip.base.BaseMethodHook
import com.my.televip.language.Keys
import com.my.televip.language.Translator
import com.my.televip.logging.Logger
import com.my.televip.obfuscate.ArgsResolver
import com.my.televip.obfuscate.Obfuscate
import com.my.televip.ui.ThemeColors
import com.my.televip.utils.Utils
import com.my.televip.virtuals.ActionBar.SimpleTextView
import com.my.televip.virtuals.ui.ProfileActivity
import de.robv.android.xposed.XposedHelpers

object EditOnlineTextView {

    @JvmField
    var isEnable: Boolean = false

    @JvmStatic
    fun init() {
        try {
            if (!isEnable) {
                isEnable = true
                val profClass = ClassLoad.getClass(ClassNames.PROFILE_ACTIVITY)
                if (profClass != null) {
                    val types: Array<Class<*>> = arrayOf(Boolean::class.javaPrimitiveType!!)
                    val merged = ArgsResolver.merge("updateProfileData", types, object : BaseMethodHook() {
                        override fun afterMethod(param: MethodHookParam) {
                            val profileActivity = ProfileActivity(param.thisObject)
                            val onlineTextViewArray = profileActivity.onlineTextView

                            if (onlineTextViewArray != null && onlineTextViewArray.size > 1) {
                                if (ConfigManager.hideOnline?.isEnable == true) {
                                    val simpleTextView = SimpleTextView(onlineTextViewArray[1])
                                    if (simpleTextView.getSimpleTextView() != null) {
                                        if (profileActivity.userId != 0L && profileActivity.userId == profileActivity.baseFragment?.userConfig?.clientUserId) {
                                            simpleTextView.setText(Translator.get(Keys.UserOffline))
                                        }
                                    }
                                }

                                if (ConfigManager.showUserID?.isEnable == true && onlineTextViewArray.size > 3) {
                                    val simpleTextView2 = SimpleTextView(onlineTextViewArray[3])
                                    val view = simpleTextView2.getSimpleTextView()
                                    if (view != null) {
                                        val oldText = simpleTextView2.getText() ?: ""
                                        val sb = SpannableStringBuilder()
                                        sb.append("\u200E")
                                        sb.append("\u200F")
                                        sb.append(oldText)
                                        sb.append("\n")

                                        val start = sb.length
                                        val id = getID(profileActivity).toString()
                                        sb.append("ID: ").append(id)

                                        sb.setSpan(RelativeSizeSpan(1.0f), start, sb.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                                        sb.setSpan(ForegroundColorSpan(ThemeColors.getTextColor()), start, sb.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

                                        simpleTextView2.setMaxLines(2)
                                        simpleTextView2.setText(sb, true)
                                        view.setOnClickListener {
                                            val name = Translator.get(Keys.Copied, id)
                                            val clipboard = Utils.getCurrentActivity()?.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
                                            clipboard?.setPrimaryClip(ClipData.newPlainText("clipboard", id))
                                            Toast.makeText(Utils.getCurrentActivity(), name, Toast.LENGTH_LONG).show()
                                        }
                                    }
                                }
                            }
                        }
                    })
                    if (merged != null) {
                        XposedHelpers.findAndHookMethod(
                            profClass,
                            Obfuscate.getMethodName("ProfileActivity", "updateProfileData"),
                            *merged
                        )
                    }
                }
            }
        } catch (t: Throwable) {
            Logger.e(t)
        }
    }

    private fun getID(profile: ProfileActivity): Long {
        return if (profile.userId > 1) {
            profile.userId
        } else {
            profile.chatId
        }
    }
}
