package com.my.televip.virtuals.ui.Cells

import android.annotation.SuppressLint
import android.content.Context
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.FrameLayout
import android.widget.TextView
import com.my.televip.application.AndroidUtilities
import com.my.televip.virtuals.Theme
import com.my.televip.virtuals.messenger.LocaleController
import com.my.televip.virtuals.ui.Components.LayoutHelper
import com.my.televip.virtuals.ui.Components.RadioButton

class RadioColorCell @SuppressLint("RtlHardcoded") constructor(context: Context) : FrameLayout(context) {

    private val textView: TextView = TextView(context)
    private val text2View: TextView = TextView(context)
    private val radioButton: RadioButton = RadioButton(context)
    @JvmField var heightDp: Int = 50

    init {
        radioButton.setSize(AndroidUtilities.dp(20f))
        addView(
            radioButton,
            LayoutHelper.createFrame(
                22,
                22f,
                (if (LocaleController.isRTL()) Gravity.RIGHT else Gravity.LEFT) or Gravity.TOP,
                (if (LocaleController.isRTL()) 0 else 18).toFloat(),
                14f,
                (if (LocaleController.isRTL()) 18 else 0).toFloat(),
                0f
            )
        )

        textView.setTextColor(Theme.getTextColor())
        textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16f)
        textView.setLines(1)
        textView.maxLines = 1
        textView.isSingleLine = true
        textView.gravity = (if (LocaleController.isRTL()) Gravity.RIGHT else Gravity.LEFT) or Gravity.CENTER_VERTICAL
        addView(
            textView,
            LayoutHelper.createFrame(
                LayoutHelper.WRAP_CONTENT,
                LayoutHelper.WRAP_CONTENT.toFloat(),
                (if (LocaleController.isRTL()) Gravity.RIGHT else Gravity.LEFT) or Gravity.TOP,
                (if (LocaleController.isRTL()) 21 else 51).toFloat(),
                13f,
                (if (LocaleController.isRTL()) 51 else 21).toFloat(),
                0f
            )
        )

        text2View.setTextColor(Theme.getTextColor())
        text2View.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14f)
        text2View.gravity = (if (LocaleController.isRTL()) Gravity.RIGHT else Gravity.LEFT) or Gravity.CENTER_VERTICAL
        text2View.visibility = GONE
        addView(
            text2View,
            LayoutHelper.createFrame(
                LayoutHelper.WRAP_CONTENT,
                LayoutHelper.WRAP_CONTENT.toFloat(),
                (if (LocaleController.isRTL()) Gravity.RIGHT else Gravity.LEFT) or Gravity.TOP,
                (if (LocaleController.isRTL()) 21 else 51).toFloat(),
                (13 + 16 + 8).toFloat(),
                (if (LocaleController.isRTL()) 51 else 21).toFloat(),
                0f
            )
        )
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (text2View.visibility == VISIBLE) {
            text2View.measure(
                MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec) - AndroidUtilities.dp((21 + 51).toFloat()), MeasureSpec.EXACTLY),
                heightMeasureSpec
            )
        }
        super.onMeasure(
            MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(
                AndroidUtilities.dp(heightDp.toFloat()) + if (text2View.visibility == VISIBLE) AndroidUtilities.dp(4f) + text2View.measuredHeight else 0,
                MeasureSpec.EXACTLY
            )
        )
    }

    fun setCheckColor(color1: Int, color2: Int) {
        radioButton.setColor(color1, color2)
    }

    fun setTextAndValue(text: CharSequence?, checked: Boolean) {
        textView.text = text
        text2View.visibility = GONE
        radioButton.setChecked(checked, false)
    }

    override fun onInitializeAccessibilityNodeInfo(info: AccessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(info)
        info.className = "android.widget.RadioButton"
        info.isCheckable = true
        info.isChecked = radioButton.isChecked()
    }
}
