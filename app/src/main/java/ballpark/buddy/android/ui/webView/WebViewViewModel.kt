package ballpark.buddy.android.ui.webView

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import ballpark.buddy.android.base.domain.BaseViewModel
import ballpark.buddy.android.databinding.WebviewFragmentBinding
import javax.inject.Inject

@HiltViewModel
class WebViewViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
): BaseViewModel() {

    val webUrl: String by lazy {
        savedStateHandle.get<String>("webUrl")
            ?: throw IllegalAccessException("URL must not be null")
    }

    @SuppressLint("SetJavaScriptEnabled")
    fun loadWebData(binding : WebviewFragmentBinding){
        setLoading(true)
        binding.webview.settings.javaScriptEnabled = true
        binding.webview.clearHistory()
        binding.webview.clearCache(false)
        binding.webview.getSettings().domStorageEnabled = true
        binding.webview.getSettings().cacheMode = WebSettings.LOAD_NO_CACHE
        binding.webview.loadUrl(webUrl)
        binding.webview.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
                val url = request?.url.toString()
                view?.loadUrl(url)
                return super.shouldOverrideUrlLoading(view, request)
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                setLoading(false)
            }

            override fun onReceivedError(view: WebView, request: WebResourceRequest, error: WebResourceError) {
                super.onReceivedError(view, request, error)
                setLoading(false)
            }
        }
    }

}