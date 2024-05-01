package ballpark.buddy.android.base.presentation

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter

abstract class BaseRecyclerViewAdapter<T, VH : BaseViewHolder<T>>(diffUtil: DiffUtil.ItemCallback<T>) :
    ListAdapter<T, VH>(diffUtil) {

    var listItems: ArrayList<T> = arrayListOf()
        @SuppressLint("NotifyDataSetChanged")
        set(value) = run {
            field = value
            notifyDataSetChanged()
        }

    abstract val itemClickListener: (T) -> Unit
    override fun getItemCount(): Int = listItems.size

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.onBind(listItems[position], position)
    }
}
