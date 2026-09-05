package com.my.televip.features.otherFeatures

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import com.my.televip.Class.ClassLoad
import com.my.televip.Class.ClassNames
import com.my.televip.base.AbstractMethodHook
import com.my.televip.hooks.HMethod
import com.my.televip.language.Keys
import com.my.televip.language.Translator
import com.my.televip.logging.Logger
import com.my.televip.obfuscate.AutomationResolver
import com.my.televip.virtuals.ActionBar.SimpleTextView
import com.my.televip.virtuals.ui.ProfileActivity

object CopyNameHook {
    @JvmStatic
    fun init(context: Context) {
        try {
            val profileActivityClass = ClassLoad.getClass(ClassNames.PROFILE_ACTIVITY)
            if (profileActivityClass != null) {
                HMethod.hookMethod(
                    profileActivityClass,
                    AutomationResolver.resolve("ProfileActivity", "createView", AutomationResolver.ResolverType.Method),
                    *AutomationResolver.merge(
                        AutomationResolver.resolveObject("createView", arrayOf<Class<*>>(Context::class.java)),
                        object : AbstractMethodHook() {
                            override fun afterMethod(param: MethodHookParam) {
                                val profileActivity = ProfileActivity(param.thisObject)
                                val nameTextViewArray = profileActivity.nameTextView
                                if (nameTextViewArray != null && nameTextViewArray.size > 1) {
                                    val simpleTextView = SimpleTextView(nameTextViewArray[1])
                                    val view = simpleTextView.simpleTextView
                                    if (view != null) {
                                        view.setOnClickListener {
                                            val text = simpleTextView.text
                                            if (text != null) {
                                                val name = Translator.get(Keys.Copied, text)
                                                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                                clipboard.setPrimaryClip(ClipData.newPlainText("clipboard", text))
                                                Toast.makeText(context, name, Toast.LENGTH_LONG).show()
                                            }
                                        }
                                    }
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
}
