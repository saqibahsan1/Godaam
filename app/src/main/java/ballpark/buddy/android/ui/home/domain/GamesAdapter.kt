package ballpark.buddy.android.ui.home.domain

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import ballpark.buddy.android.base.presentation.BaseRecyclerViewAdapter
import ballpark.buddy.android.base.presentation.BaseViewHolder
import ballpark.buddy.android.databinding.CustomItemPostsListBinding
import ballpark.buddy.android.extentions.inflater
import ballpark.buddy.android.ui.home.data.HomeUiData

class GamesAdapter(override val itemClickListener: (HomeUiData) -> Unit) :
    BaseRecyclerViewAdapter<HomeUiData, GamesAdapter.GamesViewHolder>(diffUtil) {

    inner class GamesViewHolder(private val binding: CustomItemPostsListBinding) :
        BaseViewHolder<HomeUiData>(binding.root) {
        override fun onBind(item: HomeUiData, position: Int) {
            binding.uiData = item
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GamesViewHolder {
        return GamesViewHolder(
            CustomItemPostsListBinding.inflate(
                parent.inflater,
                parent,
                false
            )
        )
    }
}

private val diffUtil = object : DiffUtil.ItemCallback<HomeUiData>() {
    override fun areItemsTheSame(oldItem: HomeUiData, newItem: HomeUiData): Boolean {
        return oldItem.gameId == newItem.gameId
    }

    override fun areContentsTheSame(
        oldItem: HomeUiData,
        newItem: HomeUiData
    ): Boolean {
        return oldItem == newItem
    }
}