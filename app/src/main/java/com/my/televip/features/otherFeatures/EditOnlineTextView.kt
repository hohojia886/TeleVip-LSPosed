package com.my.televip.features.otherFeatures

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.widget.Toast
import com.my.televip.Class.ClassLoad
import com.my.televip.Class.ClassNames
import com.my.televip.Configs.ConfigManager
import com.my.televip.base.AbstractMethodHook
import com.my.televip.hooks.HMethod
import com.my.televip.language.Keys
import com.my.televip.language.Translator
import com.my.televip.logging.Logger
import com.my.televip.obfuscate.AutomationResolver
import com.my.televip.virtuals.ActionBar.SimpleTextView
import com.my.televip.virtuals.Theme
import com.my.televip.virtuals.ui.ProfileActivity

object EditOnlineTextView {
    @JvmField var isEnable: Boolean = false

    @JvmStatic
    fun init(context: Context) {
        try {
            if (!isEnable) {
                isEnable = true
                val profileActivityClass = ClassLoad.getClass(ClassNames.PROFILE_ACTIVITY)
                if (profileActivityClass != null) {
                    HMethod.hookMethod(
                        profileActivityClass,
                        AutomationResolver.resolve("ProfileActivity", "updateProfileData", AutomationResolver.ResolverType.Method),
                        *AutomationResolver.merge(
                            AutomationResolver.resolveObject("updateProfileData", arrayOf<Class<*>>(Boolean::class.javaPrimitiveType!!)),
                            object : AbstractMethodHook() {
                                override fun afterMethod(param: MethodHookParam) {
                                    val profileActivity = ProfileActivity(param.thisObject)
                                    val onlineTextViewArray = profileActivity.onlineTextView
                                    if (onlineTextViewArray != null && onlineTextViewArray.size > 1) {
                                        if (ConfigManager.hideOnline.isEnable) {
                                            val simpleTextView = SimpleTextView(onlineTextViewArray[1])
                                            if (simpleTextView.simpleTextView != null) {
                                                if (profileActivity.userId != 0L && profileActivity.userId == profileActivity.baseFragment.userConfig.clientUserId) {
                                                    simpleTextView.setText(Translator.get(Keys.UserOffline))
                                                }
                                            }
                                        }

                                        if (ConfigManager.showUserID.isEnable) {
                                            val simpleTextView2 = SimpleTextView(onlineTextViewArray[3])
                                            if (simpleTextView2.simpleTextView != null) {
                                                val oldText = simpleTextView2.text
                                                val sb = SpannableStringBuilder()
                                                sb.append("\u200E")
                                                sb.append("\u200F")
                                                sb.append(oldText)
                                                sb.append("\n")

                                                val start = sb.length
                                                val idStr = getID(profileActivity).toString()
                                                sb.append("ID: ").append(idStr)

                                                sb.setSpan(RelativeSizeSpan(1.0f), start, sb.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                                                sb.setSpan(ForegroundColorSpan(Theme.getTextColor()), start, sb.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

                                                simpleTextView2.setMaxLines(2)
                                                simpleTextView2.setText(sb, true)
                                                simpleTextView2.simpleTextView.setOnClickListener {
                                                    if (simpleTextView2.text != null) {
                                                        val name = Translator.get(Keys.Copied, idStr)
                                                        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                                        clipboard.setPrimaryClip(ClipData.newPlainText("clipboard", idStr))
                                                        Toast.makeText(context, name, Toast.LENGTH_LONG).show()
                                                    }
                                                }
                                            }
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

    private fun getID(profile: ProfileActivity): Long {
        return if (profile.userId > 1) {
            profile.userId
        } else {
            profile.chatId
        }
    }
}
