package com.my.televip.utils

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.widget.LinearLayout
import com.my.televip.application.AndroidUtilities.dp
import com.my.televip.language.Keys
import com.my.televip.language.Translator
import com.my.televip.virtuals.ActionBar.AlertDialog
import com.my.televip.virtuals.Theme
import com.my.televip.virtuals.ui.Cells.RadioColorCell

object DialogUtils {

    @JvmStatic
    fun createSingleChoiceDialog(
        parentActivity: Activity,
        options: Array<String>,
        title: String,
        selected: Int,
        listener: DialogInterface.OnClickListener
    ): Dialog {
        val linearLayout = LinearLayout(parentActivity).apply {
            orientation = LinearLayout.VERTICAL
        }
        val builder = AlertDialog(parentActivity)
        for (a in options.indices) {
            val cell = RadioColorCell(parentActivity).apply {
                setPadding(dp(4f), 0, dp(4f), 0)
                setTag(a)
                setCheckColor(Theme.getTextColor(), Theme.getTextBlueColor())
                setTextAndValue(options[a], selected == a)
                setOnClickListener { v ->
                    val sel = v.tag as Int
                    builder.dismissRunnable?.run()
                    listener.onClick(null, sel)
                }
            }
            linearLayout.addView(cell)
        }

        builder.setTitle(title)
        builder.setView(linearLayout)
        builder.setPositiveButton(Translator.get(Keys.Cancel), null)
        return builder.create()
    }

    @JvmStatic
    fun showQuranAlert(context: Context) {
        val alertDialog = AlertDialog(context)
        alertDialog.setTitle("TeleVip")
        alertDialog.setMessage(Translator.get(Keys.QuranNotification))
        alertDialog.setPositiveButton(Translator.get(Keys.Done), null)
        alertDialog.show()
    }
}
