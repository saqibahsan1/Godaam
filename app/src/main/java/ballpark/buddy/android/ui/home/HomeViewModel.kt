package ballpark.buddy.android.ui.home

import android.graphics.drawable.Drawable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ballpark.buddy.android.R
import ballpark.buddy.android.base.domain.BaseViewModel
import ballpark.buddy.android.cache.SharedPreferencesManager
import ballpark.buddy.android.extentions.default
import ballpark.buddy.android.header.HeaderConfig
import ballpark.buddy.android.header.HeaderRightButtonType
import ballpark.buddy.android.resources.DrawableResourceManager
import ballpark.buddy.android.resources.StringsResourceManager
import ballpark.buddy.android.ui.home.data.GameUiData
import ballpark.buddy.android.utils.Constants
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Collections
import java.util.Date
import java.util.Locale
import javax.inject.Inject


@HiltViewModel
class HomeViewModel @Inject constructor(
    private val sharedPreferencesManager: SharedPreferencesManager,
    private val stringsResourceManager: StringsResourceManager,
    private val drawableResourceManager: DrawableResourceManager,
) : BaseViewModel() {

    private val firestore = FirebaseFirestore.getInstance()

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
            title = stringsResourceManager.getString(R.string.parentHome),
            rightButtonType = HeaderRightButtonType.Home,
            showBackButton = false
        )
    }

    private fun getDateTime(s: Long): String? {
        try {
            val sdf = SimpleDateFormat("dd MMM, yyyy", Locale.US)
            val netDate = Date(s * 1000)
            return sdf.format(netDate)
        } catch (e: Exception) {
            return e.toString()
        }
    }

    fun creditAmount() = sharedPreferencesManager.getUserObject()?.credits.default.toString()
    fun getPostsByUserID() {
        setLoading(true)
        val collectionRef = firestore.collection(Constants.GAME_TABLE_STAGE)
        collectionRef.whereEqualTo("postedBy", sharedPreferencesManager.getUserId())
            .get()
            .addOnSuccessListener { querySnapshot ->
                setLoading(false)
                val posts = querySnapshot.toObjects(GameUiData::class.java)
                val gameList = posts.map {
                    GameUiData(
                        postedDisplayTime = getDateTime(it.postTime.default),
                        postTime = it.postTime,
                        field = it.field,
                        bookKeeper = it.bookKeeper,
                        gameId = it.gameId,
                        isNewUpdates = it.isNewUpdates,
                        jobDuty = it.jobDuty,
                        leagueName = it.leagueName,
                        parent = it.parent,
                        payAmount = it.payAmount,
                        postedByName = it.postedByName,
                        streetAddress = it.streetAddress,
                        team = it.team,
                        postedBy = it.postedBy,
                    )
                }
                if (gameList.size > 1) {
                    Collections.sort(gameList) { o1, o2 -> o1?.postTime.default.compareTo(o2?.postTime.default) }
                    _homeUiData.value = gameList
                } else
                    _homeUiData.value = gameList
            }
            .addOnFailureListener { exception ->
                setLoading(false)
                Timber.e(exception.localizedMessage)
            }
    }


    fun navigateToPostGame() {
        navigate(HomeFragmentDirections.navHomeToPostGame())
    }

    fun onGameClick(data: GameUiData) {
        navigate(HomeFragmentDirections.navHomeToUpdateGame(data))
    }


}