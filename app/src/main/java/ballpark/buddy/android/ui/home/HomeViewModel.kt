package ballpark.buddy.android.ui.home

import android.graphics.drawable.Drawable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ballpark.buddy.android.R
import ballpark.buddy.android.base.domain.BaseViewModel
import ballpark.buddy.android.extentions.EMPTY_STRING
import ballpark.buddy.android.header.HeaderConfig
import ballpark.buddy.android.header.HeaderRightButtonType
import ballpark.buddy.android.resources.DrawableResourceManager
import ballpark.buddy.android.resources.StringsResourceManager
import ballpark.buddy.android.ui.home.data.GameUiData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
//    private val sharedPreferencesManager: SharedPreferencesManager,
    private val stringsResourceManager: StringsResourceManager,
    private val drawableResourceManager: DrawableResourceManager,
) : BaseViewModel() {

//    private val firestore = FirebaseFirestore.getInstance()

    val homeUiLiveData: LiveData<List<GameUiData>>
        get() = _homeUiData
    private val _homeUiData: MutableLiveData<List<GameUiData>> by lazy {
        MutableLiveData()
    }

    override fun getHeaderConfig(
        background: Drawable?,
        title: String,
        rightButtonType: HeaderRightButtonType,
        showBackButton: Boolean
    ): HeaderConfig {
        return super.getHeaderConfig(
            background = drawableResourceManager.getDrawable(R.drawable.common_square),
            title = EMPTY_STRING,
            rightButtonType = HeaderRightButtonType.Home,
            showBackButton = false
        )
    }

    fun navigateToPostGame() {
        navigate(HomeFragmentDirections.navHomeToPostGame())
    }

    fun onGameClick(data: GameUiData) {
        navigate(HomeFragmentDirections.navHomeToUpdateGame(data))
    }


}