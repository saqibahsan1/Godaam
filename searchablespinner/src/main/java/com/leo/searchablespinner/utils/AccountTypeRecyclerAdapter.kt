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
import com.leo.searchablespinner.utils.data.AccountItems
import com.leo.searchablespinner.utils.data.LeagueItems
import com.leo.searchablespinner.utils.data.NitaqatDropDownDataItem


internal class AccountTypeRecyclerAdapter(
    private val context: Context,
    private val list: List<AccountItems>,
    private val onItemSelectListener: OnItemSelectListener
) :
    RecyclerView.Adapter<AccountTypeRecyclerAdapter.SpinnerHolder>() {

    private var spinnerListItems: List<AccountItems> = list
    private lateinit var selectedItem: AccountItems
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
        holder.textViewSpinnerItem.text = item.name
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
            selectedItem = item
            notifyDataSetChanged()
            onItemSelectListener.setOnItemSelectListener(
                getOriginalItemPosition(item),
                item.name.toString()
            )
        }
    }


    class SpinnerHolder(itemView: ListItemSeachableSpinnerBinding) :
        RecyclerView.ViewHolder(itemView.root) {
        val textViewSpinnerItem: TextView = itemView.textViewSpinnerItem
    }

    fun filter(query: CharSequence?) {
        val filteredNames: ArrayList<AccountItems> = ArrayList()
        if (query.isNullOrEmpty()) {
            filterList(list)
        } else {
            for (s in list) {
                if (s.name?.contains(query, true) == true) {
                    filteredNames.add(s)
                }
            }
            filterList(filteredNames)
        }
    }

    private fun filterList(filteredNames: List<AccountItems>) {
        spinnerListItems = filteredNames
        notifyDataSetChanged()
    }

    private fun getOriginalItemPosition(selectedString: AccountItems): Int {
        return list.indexOf(selectedString)
    }
}