package ballpark.buddy.android.ui.home

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import ballpark.buddy.network.providers.AppLocales
import ballpark.buddy.android.base.domain.BaseViewModel
import ballpark.buddy.android.cache.SharedPreferencesManager
import ballpark.buddy.android.databinding.FragmentHomeBinding
import ballpark.buddy.android.extentions.default
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val sharedPreferencesManager: SharedPreferencesManager
) : BaseViewModel() {

    private val _url = MutableLiveData<String>().apply {
        value = "https://qiwa.sa/"
    }
    private val qiwaUrl: LiveData<String> = _url

    @SuppressLint("SetJavaScriptEnabled")
    fun loadWebData(binding: FragmentHomeBinding) {
        setLoading(true)
        binding.webview.settings.javaScriptEnabled = true
        binding.webview.clearHistory()
        binding.webview.clearCache(false)
        binding.webview.getSettings().domStorageEnabled = true
        binding.webview.getSettings().cacheMode = WebSettings.LOAD_NO_CACHE
        binding.webview.setWebChromeClient(WebChromeClient())
        binding.webview.loadUrl(qiwaUrl.value.toString())
        binding.webview.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
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

            override fun onReceivedError(
                view: WebView,
                request: WebResourceRequest,
                error: WebResourceError
            ) {
                super.onReceivedError(view, request, error)
                setLoading(false)
            }

            override fun doUpdateVisitedHistory(view: WebView?, url: String?, isReload: Boolean) {
                if (url?.contains("en").default)
                    sharedPreferencesManager.run {
                        setLocale(AppLocales.English.code)
                    }
                else
                    sharedPreferencesManager.run {
                        setLocale(AppLocales.Arabic.code)
                    }
                super.doUpdateVisitedHistory(view, url, isReload)
            }
        }
    }
}