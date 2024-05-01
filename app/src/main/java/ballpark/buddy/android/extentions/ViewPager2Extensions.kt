package ballpark.buddy.android.extentions

import androidx.databinding.BindingAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import ballpark.buddy.android.base.presentation.BaseRecyclerViewAdapter

fun ViewPager2.moveToNextItem(smoothScroll: Boolean = true) = kotlin.run {
    if (nextItem <= itemCount) {
        setCurrentItem(nextItem, smoothScroll)
    }
}

fun ViewPager2.moveToItem(position: Int, smoothScroll: Boolean = true) = kotlin.run {
    if (position <= itemCount) {
        setCurrentItem(position, smoothScroll)
    }
}

fun ViewPager2.moveToPreviousItem(smoothScroll: Boolean = true) = kotlin.run {
    if (previousItem >= initialItem) {
        setCurrentItem(previousItem, smoothScroll)
    }
}

val ViewPager2.nextItem
    get() = currentItem.plus(1)

val ViewPager2.previousItem
    get() = currentItem.minus(1)

val ViewPager2.initialItem
    get() = 0

val ViewPager2.itemCount
    get() = adapter?.itemCount ?: 0

fun ViewPager2.currentPageIsLastItem() =
    currentItem == itemCount - 1

fun ViewPager2.currentPageIsFirstItem() =
    currentItem == initialItem

fun ViewPager.moveToNextItem(smoothScroll: Boolean = true) = kotlin.run {
    if (nextItem <= itemCount) {
        setCurrentItem(nextItem, smoothScroll)
    }
}

fun ViewPager.moveToPreviousItem(smoothScroll: Boolean = true) = kotlin.run {
    if (previousItem >= initialItem) {
        setCurrentItem(previousItem, smoothScroll)
    }
}

val ViewPager.nextItem
    get() = currentItem.plus(1)

val ViewPager.previousItem
    get() = currentItem.minus(1)

val ViewPager.initialItem
    get() = 0

val ViewPager.itemCount: Int
    get() = adapter?.count ?: 0

fun ViewPager.currentPageIsLastItem() =
    currentItem == itemCount - 1

fun ViewPager.currentPageIsFirstItem() =
    currentItem == initialItem

@BindingAdapter("set_recycler_view_adapter")
fun ViewPager2.setRecyclerViewAdapter(mAdapter: BaseRecyclerViewAdapter<*, *>?) {
    mAdapter?.let {
        adapter = mAdapter
    }
}

@BindingAdapter("user_input_enabled")
fun ViewPager2.configureUserInputEnabled(enabled: Boolean?) {
    isUserInputEnabled = enabled.default
}

@BindingAdapter("off_screen_page_limit")
fun ViewPager2.configureOffScreenPageLimit(limit: Int?) {
    offscreenPageLimit = limit.default(1)
}