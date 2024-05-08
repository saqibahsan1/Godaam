package ballpark.buddy.android.header

import android.graphics.drawable.Drawable
import ballpark.buddy.android.R
import ballpark.buddy.android.click_callback.ClickCallback
import ballpark.buddy.android.click_callback.EMPTY_CLICK_CALL_BACK
import ballpark.buddy.android.extentions.EMPTY_STRING

data class HeaderConfig(
    val itemBackground: Drawable? = null,
    val titleConfig: TitleConfig,
    val headerRightButtonConfig: HeaderRightButtonConfig,
    val backButtonConfig: BackButtonConfig
) {

    companion object {
        internal val DEFAULT = HeaderConfig(
            titleConfig = TitleConfig.DEFAULT,
            headerRightButtonConfig = HeaderRightButtonConfig.DEFAULT,
            backButtonConfig = BackButtonConfig.DEFAULT
        )
    }
}

data class TitleConfig(
    val title: String,
    val textColor: Int,
    val textSize: Int
) {
    companion object {
        internal val DEFAULT = TitleConfig(
            title = EMPTY_STRING,
            textColor = R.color.black,
            textSize = com.intuit.sdp.R.dimen._15sdp
        )
    }
}


data class BackButtonConfig(
    val canShowBackButton: Boolean,
    val backButtonIconRes: Int = R.drawable.ic_app_back_button,
    val clickCallback: ClickCallback<Unit> = EMPTY_CLICK_CALL_BACK
) {
    companion object {
        internal val DEFAULT = BackButtonConfig(canShowBackButton = false)
    }
}


sealed interface HeaderRightButtonType {
    data object Home : HeaderRightButtonType
    data object None : HeaderRightButtonType
}

data class HeaderRightButtonConfig(
    val headerRightButtonType: HeaderRightButtonType,
    val clickCallback: ClickCallback<HeaderRightButtonType> = EMPTY_CLICK_CALL_BACK
) {
    companion object {
        internal val DEFAULT =
            HeaderRightButtonConfig(
                headerRightButtonType = HeaderRightButtonType.Home
            )
    }
}


