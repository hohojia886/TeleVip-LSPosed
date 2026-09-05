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
import com.my.televip.application.AndroidUtilities
import com.my.televip.virtuals.Theme

class MainToolBar(context: Context) : LinearLayout(context) {

    lateinit var title: TextView
    lateinit var image: ImageView

    init {
        createToolbar()
    }

    @SuppressLint("DiscouragedApi", "InternalInsetResource")
    private fun getStatusBarHeight(context: Context): Int {
        var result = 0
        val resourceId = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = context.resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    private fun createToolbar() {
        val statusBar = getStatusBarHeight(context)

        orientation = HORIZONTAL
        layoutParams = LayoutParams(
            LayoutParams.MATCH_PARENT,
            AndroidUtilities.dp(56f) + statusBar
        )

        setBackgroundColor(Theme.getToolBarColor())
        fitsSystemWindows = false
        gravity = Gravity.BOTTOM or Gravity.CENTER_VERTICAL

        setPadding(
            AndroidUtilities.dp(8f),
            statusBar,
            AndroidUtilities.dp(16f),
            0
        )

        val img = ImageView(context)
        image = img

        val iconParams = LayoutParams(
            AndroidUtilities.dp(40f),
            AndroidUtilities.dp(40f)
        )
        iconParams.gravity = Gravity.CENTER_VERTICAL
        img.layoutParams = iconParams
        img.setPadding(
            AndroidUtilities.dp(8f),
            AndroidUtilities.dp(8f),
            AndroidUtilities.dp(8f),
            AndroidUtilities.dp(8f)
        )
        img.scaleType = ImageView.ScaleType.FIT_CENTER

        val txt = TextView(context)
        title = txt
        txt.textSize = 18f
        txt.typeface = Typeface.DEFAULT_BOLD
        txt.isSingleLine = true
        txt.ellipsize = TextUtils.TruncateAt.END

        val titleParams = LayoutParams(
            0,
            LayoutParams.WRAP_CONTENT,
            1f
        )
        titleParams.gravity = Gravity.CENTER_VERTICAL
        titleParams.setMargins(AndroidUtilities.dp(8f), 0, 0, 0)
        txt.layoutParams = titleParams

        addView(img)
        addView(txt)
    }

    fun setTextTitle(title: CharSequence?) {
        this.title.text = title
    }

    fun setColorTitle(colorTitle: Int) {
        this.title.setTextColor(colorTitle)
    }

    fun setImageDrawable(drawable: Drawable?) {
        this.image.setImageDrawable(drawable)
    }

    fun setRippleColor(color: Int) {
        val rippleDrawable = RippleDrawable(
            ColorStateList.valueOf(color),
            null,
            null
        )
        image.background = rippleDrawable
    }
}
