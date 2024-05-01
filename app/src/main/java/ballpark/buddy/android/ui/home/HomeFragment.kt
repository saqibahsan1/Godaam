package ballpark.buddy.android.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import ballpark.buddy.android.base.domain.BaseViewModel
import ballpark.buddy.android.base.presentation.BaseFragment
import ballpark.buddy.android.databinding.FragmentHomeBinding

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    private val homeViewModel: HomeViewModel by viewModels()
    override fun getBindView(inflater: LayoutInflater, container: ViewGroup?): FragmentHomeBinding {
       return FragmentHomeBinding.inflate(inflater, container, false)
    }

    override val viewModel: BaseViewModel
        get() = homeViewModel


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeViewModel.loadWebData(binding)
    }



}