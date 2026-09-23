package com.my.televip.ui.Cells

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
import com.my.televip.virtuals.ui.Cells.TextCheckCell

class ExpandableTextCheckCell(context: Context) : LinearLayout(context) {

    private val textCheckCell: TextCheckCell
    private val childContainer: LinearLayout
    private val children: MutableList<ChildRow> = ArrayList()

    private var expanded = false
    private var syncing = false
    private var resIdRow = 0

    init {
        orientation = VERTICAL

        textCheckCell = Bridge.createTextCheckCell(context)
        addView(
            textCheckCell.view,
            LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        )

        childContainer = LinearLayout(context).apply {
            orientation = VERTICAL
            visibility = GONE
        }

        textCheckCell.view?.setOnLongClickListener {
            SettingsAdapter.playAudio(context)
            true
        }
        addView(
            childContainer,
            LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        )

        textCheckCell.view?.setOnClickListener {
            setExpanded(!expanded, true)
        }
    }

    fun addChildren(item: ConfigItem) {
        val context = context
        this.children.clear()
        childContainer.removeAllViews()

        val childrenItems = item.children ?: return
        for (child in childrenItems) {
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
            }
            row.addView(
                textCheckCell1.view,
                LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1f)
            )

            childContainer.addView(
                row,
                LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
            )

            val childRow = ChildRow(row, textCheckCell1)
            this.children.add(childRow)

            row.setOnClickListener {
                val newChecked = !textCheckCell1.isChecked
                textCheckCell1.setChecked(newChecked)
                child.setEnable(newChecked)
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
        var any = false
        for (c in children) {
            if (c.textCheckCell.isChecked) {
                any = true
                break
            }
        }
        syncing = true
        textCheckCell.setChecked(any)
        item.setEnable(any)
        syncing = false

        var count = 0
        for (c in children) {
            if (c.textCheckCell.isChecked) {
                count++
            }
        }
        textCheckCell.setTextAndCheck(
            "${Translator.get(item.key ?: "")} $count/${children.size}",
            item.isEnable,
            false
        )
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
