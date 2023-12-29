package qiwa.gov.sa.host

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import qiwa.gov.network.DefaultNetworkPreferencesManager
import qiwa.gov.network.NetworkPreferencesManager
import qiwa.gov.network.providers.AppLocaleProvider
import qiwa.gov.network.providers.DefaultAppLocaleProvider
import qiwa.gov.sa.R
import qiwa.gov.sa.base.domain.ContextWrapper
import qiwa.gov.sa.base.presentation.ProgressLoader
import qiwa.gov.sa.databinding.ActivityMainBinding
import qiwa.gov.sa.extentions.hideKeyboard
import qiwa.gov.sa.extentions.inverse
import java.util.Locale


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

        binding.appBarMain.fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                .setAction("Action", null)
                .setAnchorView(R.id.fab).show()
        }
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_calculators
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
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

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}