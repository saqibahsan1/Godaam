package ballpark.buddy.android.ui.webView

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ballpark.buddy.android.base.domain.BaseViewModel
import ballpark.buddy.android.base.presentation.BaseFragment
import ballpark.buddy.android.databinding.WebviewFragmentBinding

@AndroidEntryPoint
class WebviewFragment : BaseFragment<WebviewFragmentBinding>() {
    private val webviewViewModel : WebViewViewModel by viewModels()
    override fun getBindView(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): WebviewFragmentBinding {
        return WebviewFragmentBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        webviewViewModel.loadWebData(binding)
    }

    override val viewModel: BaseViewModel
        get() = webviewViewModel

    override fun showFooter(): Boolean = false
}