package com.my.televip.ui.Cells

import android.annotation.SuppressLint
import android.content.Context
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.accessibility.AccessibilityNodeInfo
import android.widget.FrameLayout
import android.widget.TextView
import com.my.televip.application.AndroidUtilities.dp
import com.my.televip.ui.ThemeColors
import com.my.televip.virtuals.messenger.LocaleController
import com.my.televip.virtuals.ui.Components.LayoutHelper
import com.my.televip.virtuals.ui.Components.RadioButton

@SuppressLint("RtlHardcoded")
class RadioColorCell(context: Context) : FrameLayout(context) {

    private val textView: TextView
    private val text2View: TextView
    private val radioButton: RadioButton
    var heightDp: Int = 50

    init {
        radioButton = RadioButton(context)
        radioButton.setSize(dp(20f))
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

        textView = TextView(context).apply {
            setTextColor(ThemeColors.getTextColor())
            setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16f)
            setLines(1)
            maxLines = 1
            isSingleLine = true
            gravity = (if (LocaleController.isRTL()) Gravity.RIGHT else Gravity.LEFT) or Gravity.CENTER_VERTICAL
        }
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

        text2View = TextView(context).apply {
            setTextColor(ThemeColors.getTextColor())
            setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14f)
            gravity = (if (LocaleController.isRTL()) Gravity.RIGHT else Gravity.LEFT) or Gravity.CENTER_VERTICAL
            visibility = GONE
        }
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
                MeasureSpec.makeMeasureSpec(
                    MeasureSpec.getSize(widthMeasureSpec) - dp(21f + 51f),
                    MeasureSpec.EXACTLY
                ),
                heightMeasureSpec
            )
        }
        super.onMeasure(
            MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(widthMeasureSpec), MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(
                dp(heightDp.toFloat()) + (if (text2View.visibility == VISIBLE) dp(4f) + text2View.measuredHeight else 0),
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
