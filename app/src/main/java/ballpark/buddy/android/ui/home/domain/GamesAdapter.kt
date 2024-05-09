package ballpark.buddy.android.ui.home.domain

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import ballpark.buddy.android.base.presentation.BaseRecyclerViewAdapter
import ballpark.buddy.android.base.presentation.BaseViewHolder
import ballpark.buddy.android.databinding.CustomItemPostsListBinding
import ballpark.buddy.android.extentions.clickToAction
import ballpark.buddy.android.extentions.inflater
import ballpark.buddy.android.ui.home.data.GameUiData

class GamesAdapter(override val itemClickListener: (GameUiData) -> Unit) :
    BaseRecyclerViewAdapter<GameUiData, GamesAdapter.GamesViewHolder>(diffUtil) {

    inner class GamesViewHolder(private val binding: CustomItemPostsListBinding) :
        BaseViewHolder<GameUiData>(binding.root) {
        override fun onBind(item: GameUiData, position: Int) {
            binding.uiData = item
            binding.root.clickToAction {
                itemClickListener(item)
            }
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

private val diffUtil = object : DiffUtil.ItemCallback<GameUiData>() {
    override fun areItemsTheSame(oldItem: GameUiData, newItem: GameUiData): Boolean {
        return oldItem.gameId == newItem.gameId
    }

    override fun areContentsTheSame(
        oldItem: GameUiData,
        newItem: GameUiData
    ): Boolean {
        return oldItem == newItem
    }
}