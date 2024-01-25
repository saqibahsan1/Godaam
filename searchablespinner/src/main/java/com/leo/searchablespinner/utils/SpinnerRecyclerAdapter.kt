package com.leo.searchablespinner.utils

import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.leo.searchablespinner.R
import com.leo.searchablespinner.databinding.ListItemSeachableSpinnerBinding
import com.leo.searchablespinner.interfaces.OnItemSelectListener
import com.leo.searchablespinner.utils.data.NitaqatDropDownDataItem


internal class SpinnerRecyclerAdapter(
    private val context: Context,
    private val list: List<NitaqatDropDownDataItem>,
    private val onItemSelectListener: OnItemSelectListener
) :
    RecyclerView.Adapter<SpinnerRecyclerAdapter.SpinnerHolder>() {

    private var spinnerListItems: List<NitaqatDropDownDataItem> = list
    private lateinit var selectedItem: String
    var highlightSelectedItem: Boolean = true

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpinnerHolder {
        val itemLayout =
            ListItemSeachableSpinnerBinding.inflate(LayoutInflater.from(context), parent, false)
        return SpinnerHolder(itemLayout)
    }

    override fun getItemCount(): Int {
        return spinnerListItems.size
    }

    override fun onBindViewHolder(holder: SpinnerHolder, position: Int) {
        val item = spinnerListItems[position]
        holder.textViewSpinnerItem.text = item.Economic_Activity
        if (highlightSelectedItem && ::selectedItem.isInitialized) {
            val colorDrawable: ColorDrawable =
                if (item.equals(selectedItem)) {
                    ColorDrawable(
                        ContextCompat.getColor(
                            context,
                            R.color.separatorColor
                        )
                    )
                } else {
                    ColorDrawable(
                        ContextCompat.getColor(
                            context,
                            android.R.color.white
                        )
                    )
                }
            holder.textViewSpinnerItem.background = colorDrawable
        }
        holder.textViewSpinnerItem.setOnClickListener {
            holder.textViewSpinnerItem.isClickable = false
            selectedItem = item.Economic_Activity
            notifyDataSetChanged()
            onItemSelectListener.setOnItemSelectListener(
                getOriginalItemPosition(item),
                item.Economic_Activity
            )
        }
    }


    class SpinnerHolder(itemView: ListItemSeachableSpinnerBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        val textViewSpinnerItem: TextView = itemView.textViewSpinnerItem
    }

    fun filter(query: CharSequence?) {
        val filteredNames: ArrayList<NitaqatDropDownDataItem> = ArrayList()
        if (query.isNullOrEmpty()) {
            filterList(list)
        } else {
            for (s in list) {
                if (s.Economic_Activity.contains(query, true)) {
                    filteredNames.add(s)
                }
            }
            filterList(filteredNames)
        }
    }

    private fun filterList(filteredNames: List<NitaqatDropDownDataItem>) {
        spinnerListItems = filteredNames
        notifyDataSetChanged()
    }

    private fun getOriginalItemPosition(selectedString: NitaqatDropDownDataItem): Int {
        return list.indexOf(selectedString)
    }
}