package com.my.televip.settings.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.my.televip.settings.controller.SettingsController
import com.my.televip.settings.ui.SettingsAdapter
import com.my.televip.virtuals.TeleVip.Bridge.Bridge
import com.my.televip.virtuals.ui.Cells.HeaderCell
import com.my.televip.virtuals.ui.Cells.ShadowSectionCell
import com.my.televip.virtuals.ui.Cells.TextCheckCell
import com.my.televip.virtuals.ui.Cells.TextSettingsCell

class ListAdapter(
    @JvmField var mContext: Context,
    private val settingsController: SettingsController
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun getItemViewType(position: Int): Int {
        return SettingsAdapter.getRow(position)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val viewType = getItemViewType(position)
        SettingsAdapter.onBindViewHolder(holder, settingsController, position, viewType)
    }

    override fun getItemCount(): Int {
        return SettingsAdapter.getRowCount()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            1 -> {
                val textCheckCell = Bridge.createTextCheckCell(mContext)
                TextCheckCellHolder(textCheckCell.view!!, textCheckCell.textCell)
            }
            2 -> {
                val textSettingsCell = Bridge.createTextSettingsCell(mContext)
                TextSettingsCellHolder(textSettingsCell.view!!, textSettingsCell.textSettingsCell)
            }
            3 -> ShadowSectionCellHolder(ShadowSectionCell(mContext).view!!)
            4 -> {
                val textInfoCell = Bridge.createTextInfoCell(mContext)
                TextInfoCellHolder(textInfoCell)
            }
            5 -> {
                val expandableTextCheckCell = Bridge.createExpandableTextCheckCell(mContext)
                ExpandableTextCheckCellHolder(expandableTextCheckCell, expandableTextCheckCell)
            }
            0 -> {
                val headerCell = Bridge.createHeaderCell(mContext)
                HeaderCellHolder(headerCell.view!!, headerCell.headerCell)
            }
            else -> {
                val headerCell = Bridge.createHeaderCell(mContext)
                HeaderCellHolder(headerCell.view!!, headerCell.headerCell)
            }
        }
    }

    class TextCheckCellHolder(view: View, @JvmField var textCheckCell: Any?) : RecyclerView.ViewHolder(view)
    class ExpandableTextCheckCellHolder(view: View, @JvmField var expandableTextCheckCell: Any?) : RecyclerView.ViewHolder(view)
    class HeaderCellHolder(view: View, @JvmField var headerCell: Any?) : RecyclerView.ViewHolder(view)
    class TextSettingsCellHolder(view: View, @JvmField var textSettingsCell: Any?) : RecyclerView.ViewHolder(view)
    class ShadowSectionCellHolder(@JvmField var view: View) : RecyclerView.ViewHolder(view)
    class TextInfoCellHolder(@JvmField var view: View) : RecyclerView.ViewHolder(view)
}
