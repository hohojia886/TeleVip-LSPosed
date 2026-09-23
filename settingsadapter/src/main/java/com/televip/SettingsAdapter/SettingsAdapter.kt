package com.televip.SettingsAdapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

object SettingsAdapter {

    @JvmStatic
    fun getLayoutManager(context: Context?): LinearLayoutManager {
        return LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
    }

    class RecyclerListView(context: Context) : RecyclerView(context)

    class ListAdapter(var mContext: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        override fun getItemViewType(position: Int): Int {
            return Bridge.getRow(position)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val viewType = getItemViewType(position)
            Bridge.onBindViewHolder(holder, position, viewType)
        }

        override fun getItemCount(): Int {
            return Bridge.getRowCount()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            return when (viewType) {
                1 -> TextCheckCellHolder(FrameLayout(mContext), null)
                2 -> TextSettingsCellHolder(FrameLayout(mContext), null)
                3 -> ShadowSectionCellHolder(FrameLayout(mContext))
                4 -> TextInfoCellHolder(FrameLayout(mContext))
                5 -> ExpandableTextCheckCellHolder(FrameLayout(mContext), null)
                0 -> HeaderCellHolder(FrameLayout(mContext), null)
                else -> HeaderCellHolder(FrameLayout(mContext), null)
            }
        }
    }

    open class TextCheckCellHolder(view: View, @JvmField var textCheckCell: Any?) : RecyclerView.ViewHolder(view)
    open class ExpandableTextCheckCellHolder(view: View, @JvmField var expandableTextCheckCell: Any?) : RecyclerView.ViewHolder(view)
    open class HeaderCellHolder(view: View, @JvmField var headerCell: Any?) : RecyclerView.ViewHolder(view)
    open class TextSettingsCellHolder(view: View, @JvmField var textSettingsCell: Any?) : RecyclerView.ViewHolder(view)
    open class ShadowSectionCellHolder(@JvmField var view: View) : RecyclerView.ViewHolder(view)
    open class TextInfoCellHolder(@JvmField var view: View) : RecyclerView.ViewHolder(view)
}
