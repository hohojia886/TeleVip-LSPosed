package com.my.televip.virtuals.ActionBar

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.view.View
import com.my.televip.Class.ClassLoad
import com.my.televip.Class.ClassNames
import com.my.televip.obfuscate.AutomationResolver
import com.my.televip.utils.Utils
import de.robv.android.xposed.XposedHelpers
import java.lang.reflect.Proxy

class AlertDialog(context: Context) {

    fun interface OnClick {
        fun onClick()
    }

    @JvmField
    val alertDialog: Any = XposedHelpers.newInstance(ClassLoad.getClass(ClassNames.ALERT_DIALOG_BUILDER), context)

    fun setTitle(title: CharSequence?) {
        XposedHelpers.callMethod(
            alertDialog,
            AutomationResolver.resolve("AlertDialog\$Builder", "setTitle", AutomationResolver.ResolverType.Method),
            title
        )
    }

    fun setView(view: View) {
        XposedHelpers.callMethod(
            alertDialog,
            AutomationResolver.resolve("AlertDialog\$Builder", "setView", AutomationResolver.ResolverType.Method),
            view
        )
    }

    fun setMessage(message: CharSequence?) {
        XposedHelpers.callMethod(
            alertDialog,
            AutomationResolver.resolve("AlertDialog\$Builder", "setMessage", AutomationResolver.ResolverType.Method),
            message
        )
    }

    fun setPositiveButton(text: CharSequence?, obj: Any?) {
        XposedHelpers.callMethod(
            alertDialog,
            AutomationResolver.resolve("AlertDialog\$Builder", "setPositiveButton", AutomationResolver.ResolverType.Method),
            text, obj
        )
    }

    fun setNegativeButton(text: CharSequence?, obj: Any?) {
        XposedHelpers.callMethod(
            alertDialog,
            AutomationResolver.resolve("AlertDialog\$Builder", "setNegativeButton", AutomationResolver.ResolverType.Method),
            text, obj
        )
    }

    fun setNeutralButton(text: CharSequence?, obj: Any?) {
        XposedHelpers.callMethod(
            alertDialog,
            AutomationResolver.resolve("AlertDialog\$Builder", "setNeutralButton", AutomationResolver.ResolverType.Method),
            text, obj
        )
    }

    fun show() {
        XposedHelpers.callMethod(
            alertDialog,
            AutomationResolver.resolve("AlertDialog\$Builder", "show", AutomationResolver.ResolverType.Method)
        )
    }

    fun create(): Dialog? {
        return XposedHelpers.callMethod(
            alertDialog,
            AutomationResolver.resolve("AlertDialog\$Builder", "create", AutomationResolver.ResolverType.Method)
        ) as? Dialog
    }

    fun getDismissRunnable(): Runnable? {
        return XposedHelpers.callMethod(
            alertDialog,
            AutomationResolver.resolve("AlertDialog\$Builder", "getDismissRunnable", AutomationResolver.ResolverType.Method)
        ) as? Runnable
    }

    companion object {
        @JvmStatic
        fun click(lambda: OnClick): Any {
            val listenerClass = ClassLoad.getClass(ClassNames.ALERT_DIALOG_BUTTON_CLICK)
            return if (listenerClass != null) {
                Proxy.newProxyInstance(
                    Utils.classLoader,
                    arrayOf(listenerClass)
                ) { _, method, _ ->
                    if (method.name == AutomationResolver.resolve("AlertDialog\$OnButtonClickListener", "onClick", AutomationResolver.ResolverType.Method)) {
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
