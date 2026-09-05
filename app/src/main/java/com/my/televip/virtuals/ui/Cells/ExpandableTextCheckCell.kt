package com.my.televip.virtuals.ui.Cells

import android.animation.ValueAnimator
import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.LinearLayout
import com.my.televip.Configs.ConfigItem
import com.my.televip.language.Translator
import com.my.televip.settings.ui.SettingsAdapter
import com.my.televip.virtuals.TeleVip.Bridge.Bridge

class ExpandableTextCheckCell(context: Context) : LinearLayout(context) {

    private val textCheckCell: TextCheckCell = Bridge.createTextCheckCell(context)
    private val childContainer: LinearLayout = LinearLayout(context).apply {
        orientation = VERTICAL
        visibility = GONE
    }
    private val children = ArrayList<ChildRow>()

    private var expanded = false
    private var syncing = false
    private var resIdRow = 0

    init {
        orientation = VERTICAL
        addView(textCheckCell.getView(), LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT))

        textCheckCell.getView().setOnLongClickListener {
            SettingsAdapter.playAudio(context)
            true
        }
        addView(childContainer, LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT))

        textCheckCell.getView().setOnClickListener {
            setExpanded(!expanded, true)
        }
    }

    fun addChildren(item: ConfigItem) {
        val context = context
        this.children.clear()
        childContainer.removeAllViews()
        val childItems = item.children ?: return
        for (child in childItems) {
            if (child.type != ConfigItem.SWITCH) continue

            val textCheckCell1 = TextCheckCell(context)
            textCheckCell1.setTextAndCheck(Translator.get(child.key ?: ""), child.isEnable, false)

            val row = LinearLayout(context).apply {
                orientation = HORIZONTAL
                setBackgroundResource(resIdRow)
                setPadding(dp(), 0, 0, 0)
                setOnLongClickListener {
                    SettingsAdapter.playAudio(context)
                    true
                }
                addView(
                    textCheckCell1.getView(),
                    LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1f)
                )
            }

            childContainer.addView(
                row,
                LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            )

            val childRow = ChildRow(row, textCheckCell1)
            this.children.add(childRow)

            row.setOnClickListener {
                val newChecked = !textCheckCell1.isChecked()
                textCheckCell1.setChecked(newChecked)
                child.isEnable = newChecked
                child.run()
                updateMainStateFromChildren(item)
            }

            updateMainStateFromChildren(item)
        }
    }

    fun setExpanded(value: Boolean, animate: Boolean) {
        if (expanded == value) return
        expanded = value

        if (!animate) {
            childContainer.visibility = if (expanded) VISIBLE else GONE
            return
        }

        if (expanded) {
            childContainer.visibility = VISIBLE
            childContainer.measure(
                MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED)
            )
            val targetHeight = childContainer.measuredHeight
            animateHeight(0, targetHeight)
        } else {
            val startHeight = childContainer.height
            animateHeight(startHeight, 0)
        }
    }

    fun setBChildResource(resid: Int) {
        resIdRow = resid
    }

    private fun updateMainStateFromChildren(item: ConfigItem) {
        if (syncing) return
        val any = children.any { it.textCheckCell.isChecked() }
        syncing = true
        textCheckCell.setChecked(any)
        item.isEnable = any
        syncing = false
        val count = children.count { it.textCheckCell.isChecked() }
        textCheckCell.setTextAndCheck(Translator.get(item.key ?: "") + " " + count + "/" + children.size, item.isEnable, false)
    }

    private fun animateHeight(from: Int, to: Int) {
        val animator = ValueAnimator.ofInt(from, to).apply {
            duration = 220
            interpolator = DecelerateInterpolator()
            addUpdateListener { animation ->
                val value = animation.animatedValue as Int
                val params = childContainer.layoutParams
                params.height = if (value == 0 && to == 0) 0 else value
                childContainer.layoutParams = params
                if (value == 0 && to == 0) {
                    childContainer.visibility = GONE
                    childContainer.layoutParams.height = LayoutParams.WRAP_CONTENT
                }
            }
        }
        animator.start()
    }

    private fun dp(): Int {
        val density = resources.displayMetrics.density
        return (24 * density).toInt()
    }

    class ChildRow(
        @JvmField val rootView: LinearLayout,
        @JvmField val textCheckCell: TextCheckCell
    )
}
