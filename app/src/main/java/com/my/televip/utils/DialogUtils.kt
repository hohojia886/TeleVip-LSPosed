package com.my.televip.utils

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.widget.LinearLayout
import com.my.televip.application.AndroidUtilities.dp
import com.my.televip.language.Keys
import com.my.televip.language.Translator
import com.my.televip.ui.Cells.RadioColorCell
import com.my.televip.ui.ThemeColors
import com.my.televip.virtuals.ActionBar.AlertDialog

object DialogUtils {

    @JvmStatic
    fun createSingleChoiceDialog(
        parentActivity: Activity,
        options: Array<String>,
        title: String,
        selected: Int,
        listener: DialogInterface.OnClickListener
    ): Dialog? {
        val linearLayout = LinearLayout(parentActivity)
        linearLayout.orientation = LinearLayout.VERTICAL
        val builder = AlertDialog(parentActivity)

        for (a in options.indices) {
            val cell = RadioColorCell(parentActivity)
            cell.setPadding(dp(4f), 0, dp(4f), 0)
            cell.tag = a
            cell.setCheckColor(ThemeColors.getTextColor(), ThemeColors.getTextBlueColor())
            cell.setTextAndValue(options[a], selected == a)
            linearLayout.addView(cell)
            cell.setOnClickListener { v ->
                val sel = v.tag as Int
                builder.getDismissRunnable()?.run()
                listener.onClick(null, sel)
            }
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
