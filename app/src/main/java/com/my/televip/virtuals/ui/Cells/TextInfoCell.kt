package com.my.televip.virtuals.ui.Cells

import android.content.Context
import android.util.TypedValue
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.TextView
import com.my.televip.application.AndroidUtilities
import com.my.televip.virtuals.Theme
import com.my.televip.virtuals.ui.Components.LayoutHelper

class TextInfoCell(context: Context) : FrameLayout(context) {

    @JvmField
    val textView: TextView = TextView(context)

    init {
        textView.setTextColor(Theme.getTextGrayColor())
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 13f)
        textView.gravity = Gravity.CENTER
        textView.setPadding(0, AndroidUtilities.dp(19f), 0, AndroidUtilities.dp(19f))
        addView(
            textView,
            LayoutHelper.createFrame(
                LayoutHelper.WRAP_CONTENT,
                LayoutHelper.WRAP_CONTENT.toFloat(),
                Gravity.CENTER,
                17f, 0f, 17f, 0f
            )
        )
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(
            MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
        )
    }
}
