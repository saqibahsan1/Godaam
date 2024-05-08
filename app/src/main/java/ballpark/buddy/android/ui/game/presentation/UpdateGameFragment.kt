package ballpark.buddy.android.ui.game.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import ballpark.buddy.android.base.domain.BaseViewModel
import ballpark.buddy.android.base.presentation.BaseFragment
import ballpark.buddy.android.databinding.UpdateGameBinding
import ballpark.buddy.android.ui.game.domain.UpdateGameViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class UpdateGameFragment : BaseFragment<UpdateGameBinding>() {

    private val updateGameViewModel: UpdateGameViewModel by viewModels()
    override fun getBindView(inflater: LayoutInflater, container: ViewGroup?): UpdateGameBinding {
        return UpdateGameBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        updateGameViewModel.searchableSpinner(requireActivity(),binding.leagueEt)
        updateGameViewModel.jobItemSpinner(requireActivity(),binding.jobDutyTextEt)
    }
    override fun showFooter(): Boolean = false
    override val viewModel: BaseViewModel
        get() = updateGameViewModel
}