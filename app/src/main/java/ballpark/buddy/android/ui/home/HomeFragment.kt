package ballpark.buddy.android.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.viewModels
import ballpark.buddy.android.base.domain.BaseViewModel
import ballpark.buddy.android.base.presentation.BaseFragment
import ballpark.buddy.android.databinding.FragmentHomeBinding
import ballpark.buddy.android.ui.home.domain.GamesAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    private val homeViewModel: HomeViewModel by viewModels()
    override fun getBindView(inflater: LayoutInflater, container: ViewGroup?): FragmentHomeBinding {
       return FragmentHomeBinding.inflate(inflater, container, false).apply {
           adapter = GamesAdapter { data->
               homeViewModel.onGameClick(data)
           }
       }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
    }

    override val viewModel: BaseViewModel
        get() = homeViewModel



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        homeViewModel.getPostsByUserID()
    }


}