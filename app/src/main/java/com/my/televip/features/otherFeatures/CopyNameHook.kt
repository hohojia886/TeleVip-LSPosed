package com.my.televip.features.otherFeatures

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Toast
import com.my.televip.Class.ClassLoad
import com.my.televip.Class.ClassNames
import com.my.televip.base.BaseMethodHook
import com.my.televip.language.Keys
import com.my.televip.language.Translator
import com.my.televip.logging.Logger
import com.my.televip.obfuscate.ArgsResolver
import com.my.televip.obfuscate.Obfuscate
import com.my.televip.utils.Utils
import com.my.televip.virtuals.ActionBar.SimpleTextView
import com.my.televip.virtuals.ui.ProfileActivity
import de.robv.android.xposed.XposedHelpers

object CopyNameHook {

    @JvmStatic
    fun init() {
        try {
            val profClass = ClassLoad.getClass(ClassNames.PROFILE_ACTIVITY)
            if (profClass != null) {
                val types: Array<Class<*>> = arrayOf(Context::class.java)
                val merged = ArgsResolver.merge("createView", types, object : BaseMethodHook() {
                    override fun afterMethod(param: MethodHookParam) {
                        val profileActivity = ProfileActivity(param.thisObject)
                        val nameTextViewArray = profileActivity.nameTextView

                        if (nameTextViewArray != null && nameTextViewArray.size > 1) {
                            val simpleTextView = SimpleTextView(nameTextViewArray[1])
                            val view = simpleTextView.getSimpleTextView()
                            if (view != null) {
                                view.setOnClickListener {
                                    val text = simpleTextView.getText()
                                    if (text != null) {
                                        val name = Translator.get(Keys.Copied, text)
                                        val clipboard = Utils.getCurrentActivity()?.getSystemService(Context.CLIPBOARD_SERVICE) as? ClipboardManager
                                        clipboard?.setPrimaryClip(ClipData.newPlainText("clipboard", text))
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
                        Obfuscate.getMethodName("ProfileActivity", "createView"),
                        *merged
                    )
                }
            }
        } catch (t: Throwable) {
            Logger.e(t)
        }
    }
}
