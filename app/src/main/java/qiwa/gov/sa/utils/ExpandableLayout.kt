package qiwa.gov.sa.utils

import android.content.Context
import android.util.AttributeSet
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.ViewDataBinding
import qiwa.gov.sa.R
import qiwa.gov.sa.databinding.ExpandableLayoutBinding
import qiwa.gov.sa.extentions.getBinding
import qiwa.gov.sa.extentions.getStyleAttributesFromBinding
import qiwa.gov.sa.extentions.greaterThan
import qiwa.gov.sa.extentions.inverse
import qiwa.gov.sa.extentions.visibility


class ExpandableLayout @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleRes: Int = 0
) : ConstraintLayout(context, attributeSet, defStyleRes) {

    private val binding = getBinding<ExpandableLayoutBinding>(R.layout.expandable_layout)

    var callback: Callback? = null
    private var isExpanded: Boolean = false
        set(value) {
            field = value
            toggleView()
        }

    private fun toggleView() {
        binding.expandablePaneParent.visibility(isExpanded)
        callback?.onExpanded(
            expanded = isExpanded,
            expandableLayout = binding.expandablePane.binding,
            topLayout = binding.nonExpandablePane.binding
        )
    }

    private var topLayout: Int = -1
        set(value) {
            field = value
            if (value.greaterThan(0)) {
                inflateTopLayout(value)
            }
        }

    private var expandableLayout: Int = -1
        set(value) {
            field = value
            if (value.greaterThan(0)) {
                inflateExpandableLayout(value)
            }
        }

    init {
        binding.getStyleAttributesFromBinding(R.styleable.ExpandableLayout, attributeSet) {
            topLayout = getResourceId(R.styleable.ExpandableLayout_topLayout, 0)
            expandableLayout = getResourceId(R.styleable.ExpandableLayout_expandableLayout, 0)
        }
        updateView()
    }

    fun setDefaultState() {
        isExpanded = false
    }

    private fun updateView() {
        isExpanded = false
        binding.nonExpandablePaneParent.setOnClickListener {
            isExpanded = isExpanded.inverse
        }
    }

    private fun inflateTopLayout(layoutRes: Int) {
        binding.nonExpandablePane.run {
            viewStub?.layoutResource = layoutRes
            viewStub?.inflate()
        }
    }

    private fun inflateExpandableLayout(layoutRes: Int) {
        binding.expandablePane.apply {
            viewStub?.layoutResource = layoutRes
            viewStub?.inflate()
        }
    }

    fun attachCallback(callback: Callback) {
        this.callback = callback
    }

    internal inline fun <reified VB : ViewDataBinding> getTopLayoutBinding(topBinding: VB.() -> Unit) {
        binding.nonExpandablePane.binding?.let {
            if (it is VB) topBinding(it)
        }
    }
    internal inline fun <reified VB : ViewDataBinding> getExpandableLayoutBinding(expandableBinding: VB.() -> Unit) {
        binding.expandablePane.binding?.let {
            if (it is VB) expandableBinding(it)
        }
    }

    @FunctionalInterface
    interface Callback {
        fun <TVB : ViewDataBinding, EVB : ViewDataBinding> onExpanded(
            expanded: Boolean,
            topLayout: TVB?,
            expandableLayout: EVB?
        )
    }
}
