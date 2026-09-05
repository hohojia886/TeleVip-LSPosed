package com.my.televip.settings.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.my.televip.settings.controller.SettingsController
import com.my.televip.settings.ui.SettingsAdapter
import com.my.televip.virtuals.TeleVip.Bridge.Bridge
import com.my.televip.virtuals.ui.Cells.ShadowSectionCell

class ListAdapter(
    @JvmField val MContext: Context,
    @JvmField val settingsController: SettingsController
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
                val textCheckCell = Bridge.createTextCheckCell(MContext)
                TextCheckCellHolder(textCheckCell.getView(), textCheckCell.textCell)
            }
            2 -> {
                val textSettingsCell = Bridge.createTextSettingsCell(MContext)
                TextSettingsCellHolder(textSettingsCell.getView(), textSettingsCell.textSettingsCell)
            }
            3 -> ShadowSectionCellHolder(ShadowSectionCell(MContext).getView())
            4 -> {
                val textInfoCell = Bridge.createTextInfoCell(MContext)
                TextInfoCellHolder(textInfoCell)
            }
            5 -> {
                val expandableTextCheckCell = Bridge.createExpandableTextCheckCell(MContext)
                ExpandableTextCheckCellHolder(expandableTextCheckCell, expandableTextCheckCell)
            }
            else -> {
                val headerCell = Bridge.createHeaderCell(MContext)
                HeaderCellHolder(headerCell.getView(), headerCell.headerCell)
            }
        }
    }

    class TextCheckCellHolder(view: View, @JvmField val textCheckCell: Any?) : RecyclerView.ViewHolder(view)
    class ExpandableTextCheckCellHolder(view: View, @JvmField val expandableTextCheckCell: Any?) : RecyclerView.ViewHolder(view)
    class HeaderCellHolder(view: View, @JvmField val headerCell: Any?) : RecyclerView.ViewHolder(view)
    class TextSettingsCellHolder(view: View, @JvmField val textSettingsCell: Any?) : RecyclerView.ViewHolder(view)
    class ShadowSectionCellHolder(@JvmField val view: View) : RecyclerView.ViewHolder(view)
    class TextInfoCellHolder(@JvmField val view: View) : RecyclerView.ViewHolder(view)
}
