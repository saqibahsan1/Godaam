package ballpark.buddy.android.extentions

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import ballpark.buddy.android.base.presentation.BaseRecyclerViewAdapter


@BindingAdapter(value = ["setAdapter"])
fun RecyclerView.bindRecyclerViewAdapter(adapter: BaseRecyclerViewAdapter<*, *>?) {
    this.adapter = adapter
}

