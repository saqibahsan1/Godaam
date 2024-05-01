package ballpark.buddy.android.ui.feedback

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ballpark.buddy.android.base.domain.BaseViewModel
import ballpark.buddy.android.base.presentation.BaseFragment
import ballpark.buddy.android.databinding.FragmentFeedbackBinding


@AndroidEntryPoint
class FeedbackFragment : BaseFragment<FragmentFeedbackBinding>() {

    private val feedbackViewModel : FeedbackViewModel by viewModels()

    override fun getBindView(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFeedbackBinding {
       return FragmentFeedbackBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.messageBox.editText?.doAfterTextChanged {
            binding.errorMessage.visibility = View.GONE
        }
    }

    override val viewModel: BaseViewModel
        get() = feedbackViewModel

}