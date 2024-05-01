package ballpark.buddy.android.host

import android.content.Context
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.ui.AppBarConfiguration
import ballpark.buddy.android.base.presentation.ProgressLoader
import ballpark.buddy.android.databinding.ActivityMainBinding
import ballpark.buddy.android.extentions.hideKeyboard
import ballpark.buddy.android.extentions.inverse
import ballpark.buddy.network.NetworkPreferencesManager
import ballpark.buddy.network.providers.AppLocaleProvider
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var appLocaleProvider: AppLocaleProvider
    private lateinit var networkPreferencesManager: NetworkPreferencesManager
    private val hostViewModel: HostViewModel by viewModels()
    private val progressLoader: ProgressLoader by lazy {
        ProgressLoader(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater).apply {
            lifecycleOwner = this@MainActivity
            viewModel = hostViewModel
        }
        setContentView(binding.root)
        setSupportActionBar(binding.appBarMain.toolbar)
        observeLiveData()
    }

    private fun showOrHideProgress(showProgress: Boolean) {
        progressLoader.showOrHideProgress(showProgress)
    }

    private fun observeLiveData() {
        hostViewModel.run {
            loadingLiveData.observe(this@MainActivity, ::showOrHideProgress)
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val view: View = currentFocus ?: return super.dispatchTouchEvent(ev)
        if ((ev.action == MotionEvent.ACTION_UP || ev.action == MotionEvent.ACTION_MOVE) &&
            view is EditText && view.javaClass.name.startsWith("android.webkit.").inverse
        ) {
            val array = IntArray(2)
            view.getLocationOnScreen(array)
            val x: Float = ev.rawX + view.left - array[0]
            val y: Float = ev.rawY + view.top - array[1]
            if (x < view.left || x > view.right || y < view.top || y > view.bottom) {
                view.hideKeyboard()
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    override fun attachBaseContext(newBase: Context?) {
//        newBase?.let {
//            networkPreferencesManager = DefaultNetworkPreferencesManager(context = newBase)
//            appLocaleProvider = DefaultAppLocaleProvider(newBase, networkPreferencesManager)
//            val locale = Locale(appLocaleProvider.getLocaleCode())
//            super.attachBaseContext(ContextWrapper.wrap(newBase, locale))
        /*} ?: */super.attachBaseContext(newBase)
    }

}