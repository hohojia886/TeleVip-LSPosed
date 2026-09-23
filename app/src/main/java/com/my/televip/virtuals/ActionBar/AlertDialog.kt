package com.my.televip.virtuals.ActionBar

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.view.View
import com.my.televip.Class.ClassLoad
import com.my.televip.Class.ClassNames
import com.my.televip.obfuscate.Obfuscate
import com.my.televip.utils.Utils
import de.robv.android.xposed.XposedHelpers
import java.lang.reflect.Proxy

class AlertDialog(context: Context?) {

    fun interface OnClick {
        fun onClick()
    }

    @JvmField
    var alertDialog: Any? = null

    init {
        val builderClass = ClassLoad.getClass(ClassNames.ALERT_DIALOG_BUILDER)
        alertDialog = XposedHelpers.newInstance(builderClass, context)
    }

    fun getAlertDialog(): Dialog? {
        val target = alertDialog ?: return null
        return XposedHelpers.getObjectField(target, Obfuscate.getFieldName("AlertDialog\$Builder", "alertDialog")) as? Dialog
    }

    fun setTitle(title: CharSequence?) {
        val target = alertDialog ?: return
        XposedHelpers.callMethod(target, Obfuscate.getMethodName("AlertDialog\$Builder", "setTitle"), title)
    }

    fun setView(view: View?) {
        val target = alertDialog ?: return
        XposedHelpers.callMethod(target, Obfuscate.getMethodName("AlertDialog\$Builder", "setView"), view)
    }

    fun setMessage(message: CharSequence?) {
        val target = alertDialog ?: return
        XposedHelpers.callMethod(target, Obfuscate.getMethodName("AlertDialog\$Builder", "setMessage"), message)
    }

    fun setPositiveButton(text: CharSequence?, obj: Any?) {
        val target = alertDialog ?: return
        XposedHelpers.callMethod(target, Obfuscate.getMethodName("AlertDialog\$Builder", "setPositiveButton"), text, obj)
    }

    fun setNegativeButton(text: CharSequence?, obj: Any?) {
        val target = alertDialog ?: return
        XposedHelpers.callMethod(target, Obfuscate.getMethodName("AlertDialog\$Builder", "setNegativeButton"), text, obj)
    }

    fun setNeutralButton(text: CharSequence?, obj: Any?) {
        val target = alertDialog ?: return
        XposedHelpers.callMethod(target, Obfuscate.getMethodName("AlertDialog\$Builder", "setNeutralButton"), text, obj)
    }

    fun show() {
        val target = alertDialog ?: return
        XposedHelpers.callMethod(target, Obfuscate.getMethodName("AlertDialog\$Builder", "show"))
    }

    fun create(): Dialog? {
        val target = alertDialog ?: return null
        return XposedHelpers.callMethod(target, Obfuscate.getMethodName("AlertDialog\$Builder", "create")) as? Dialog
    }

    fun getDismissRunnable(): Runnable? {
        val target = alertDialog ?: return null
        return XposedHelpers.callMethod(target, Obfuscate.getMethodName("AlertDialog\$Builder", "getDismissRunnable")) as? Runnable
    }

    companion object {
        @JvmStatic
        fun click(lambda: OnClick): Any? {
            val listenerClass = ClassLoad.getClass(ClassNames.ALERT_DIALOG_BUTTON_CLICK)
            return if (listenerClass != null) {
                Proxy.newProxyInstance(
                    Utils.classLoader,
                    arrayOf(listenerClass)
                ) { _, method, _ ->
                    if (method.name == Obfuscate.getMethodName("AlertDialog\$OnButtonClickListener", "onClick")) {
                        lambda.onClick()
                    }
                    null
                }
            } else {
                DialogInterface.OnClickListener { _, _ -> lambda.onClick() }
            }
        }
    }
}
