package qiwa.gov.sa.extentions

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import qiwa.gov.sa.base.presentation.BaseRecyclerViewAdapter


@BindingAdapter(value = ["setAdapter"])
fun RecyclerView.bindRecyclerViewAdapter(adapter: BaseRecyclerViewAdapter<*, *>?) {
    this.adapter = adapter
}

