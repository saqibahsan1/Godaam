package ballpark.buddy.android.ui.auth.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.viewModels
import ballpark.buddy.android.base.domain.BaseViewModel
import ballpark.buddy.android.base.presentation.BaseFragment
import ballpark.buddy.android.databinding.LoginFragmentBinding
import ballpark.buddy.android.databinding.PreLoginFragmentBinding
import ballpark.buddy.android.extentions.getDefaultAppHeaderHeight
import ballpark.buddy.android.extentions.navigate
import ballpark.buddy.android.ui.auth.domain.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PreLoginFragment : BaseFragment<PreLoginFragmentBinding>() {

    private val loginViewModel : LoginViewModel by viewModels()

    override fun getBindView(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): PreLoginFragmentBinding {
        return PreLoginFragmentBinding.inflate(inflater,container,false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }
    override val viewModel: BaseViewModel
        get() = loginViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.loginButton.setOnClickListener {
            navigate(PreLoginFragmentDirections.navPreLoginToLogin())
        }
        binding.registerButton.setOnClickListener {
            navigate(PreLoginFragmentDirections.navPreLoginToCreateAccount())
        }
    }

    override fun getFragNavContainerTopMargin(): Int = getDefaultAppHeaderHeight()
    override fun showFooter(): Boolean = false
}