package com.my.televip.ui.toolBar

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.graphics.drawable.RippleDrawable
import android.text.TextUtils
import android.view.Gravity
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.my.televip.application.AndroidUtilities.dp
import com.my.televip.ui.ThemeColors

class MainToolBar(context: Context) : LinearLayout(context) {

    private var titleView: TextView? = null
    @JvmField
    var image: ImageView? = null

    init {
        createToolbar(context)
    }

    private fun getStatusBarHeight(context: Context): Int {
        var result = 0
        @SuppressLint("InternalInsetResource", "DiscouragedApi")
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = context.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    private fun createToolbar(context: Context) {
        val statusBar = getStatusBarHeight(context)

        orientation = HORIZONTAL
        layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            dp(56f) + statusBar
        )

        setBackgroundColor(ThemeColors.getToolBarColor())
        fitsSystemWindows = false
        gravity = Gravity.BOTTOM or Gravity.CENTER_VERTICAL

        setPadding(
            dp(8f),
            statusBar,
            dp(16f),
            0
        )

        val img = ImageView(context)
        val iconParams = LayoutParams(
            dp(40f),
            dp(40f)
        ).apply {
            gravity = Gravity.CENTER_VERTICAL
        }
        img.layoutParams = iconParams
        img.setPadding(
            dp(8f),
            dp(8f),
            dp(8f),
            dp(8f)
        )
        img.scaleType = ImageView.ScaleType.FIT_CENTER
        image = img

        val title = TextView(context).apply {
            textSize = 18f
            typeface = Typeface.DEFAULT_BOLD
            isSingleLine = true
            ellipsize = TextUtils.TruncateAt.END
        }

        val titleParams = LayoutParams(
            0,
            LayoutParams.WRAP_CONTENT,
            1f
        ).apply {
            gravity = Gravity.CENTER_VERTICAL
            setMargins(dp(8f), 0, 0, 0)
        }
        title.layoutParams = titleParams
        titleView = title

        addView(img)
        addView(title)
    }

    fun setTextTitle(title: CharSequence?) {
        titleView?.text = title
    }

    fun setColorTitle(colorTitle: Int) {
        titleView?.setTextColor(colorTitle)
    }

    fun setImageDrawable(drawable: Drawable?) {
        image?.setImageDrawable(drawable)
    }

    fun setRippleColor(color: Int) {
        val rippleDrawable = RippleDrawable(
            ColorStateList.valueOf(color),
            null,
            null
        )
        image?.background = rippleDrawable
    }
}
