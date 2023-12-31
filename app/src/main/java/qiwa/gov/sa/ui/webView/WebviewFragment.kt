package qiwa.gov.sa.ui.webView

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import qiwa.gov.sa.base.domain.BaseViewModel
import qiwa.gov.sa.base.presentation.BaseFragment
import qiwa.gov.sa.databinding.WebviewFragmentBinding

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