package ballpark.buddy.android.extentions

import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import ballpark.buddy.android.base.presentation.BaseRecyclerViewAdapter
import ballpark.buddy.android.ui.home.data.GameUiData
import ballpark.buddy.android.ui.home.domain.GamesAdapter


@BindingAdapter(value = ["setAdapter"])
fun RecyclerView.bindRecyclerViewAdapter(adapter: BaseRecyclerViewAdapter<*, *>?) {
    this.adapter = adapter
}

@BindingAdapter("games_listing")
fun RecyclerView.setUpgradeCoverItems(list: List<GameUiData>?) {
    if (list?.isEmpty().default) hide() else {
        (adapter as? GamesAdapter)?.listItems = list.orEmptyArrayList()
        show()
    }
}

