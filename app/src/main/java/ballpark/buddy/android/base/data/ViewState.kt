package ballpark.buddy.android.base.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import ballpark.buddy.network.ApiErrors
import ballpark.buddy.network.ErrorWithCode
import ballpark.buddy.android.click_callback.ClickCallback
import ballpark.buddy.android.click_callback.EMPTY_CLICK_CALL_BACK
import ballpark.buddy.android.dialog.GeneralDialogUiData
import ballpark.buddy.android.dialog.NoInternetDialogUiData

sealed class ViewState<out T> {
    data class Success<T>(val data: T) : ViewState<T>()
    object Empty : ViewState<Nothing>()
    data class Loading(val loading: Boolean) : ViewState<Nothing>()
    data class Error(val uiMessage: UIMessage) : ViewState<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$uiMessage]"
            is Loading -> "Loading[loading=$loading]"
            Empty -> "Empty"
        }
    }
}

sealed interface UIMessage {
    data class DialogMessage(
        val dialogMessageType: DialogMessageType,
        val clickCallback: ClickCallback<Any> = EMPTY_CLICK_CALL_BACK
    ) : UIMessage

    data class ToastMessage(val message: String) : UIMessage
}

sealed interface DialogMessageType {
//    data class Success(val successDialogUiData: SuccessDialogUiData) : DialogMessageType
    data class Error(val message: String, val description: String? = null, var isCancelable: Boolean = true, val errorCode:Int? = null) :
        DialogMessageType
    data class GeneralDialog(val generalDialogUiData: GeneralDialogUiData) : DialogMessageType
    data class NoInternet(val noInternetDialogUiData: NoInternetDialogUiData) : DialogMessageType
}

fun getDefaultViewStateForError(error: ErrorWithCode, isCancelable: Boolean = true): ViewState.Error {
    val dialogMessageType = when (error.code) {
        ApiErrors.NO_NETWORK_CODE -> DialogMessageType.NoInternet(NoInternetDialogUiData())
        else -> DialogMessageType.Error(
            error.message,
            isCancelable = isCancelable,
            errorCode = error.code
        )
    }
    return ViewState.Error(UIMessage.DialogMessage(dialogMessageType, EMPTY_CLICK_CALL_BACK))
}

suspend fun <T> Flow<ViewState<T>>.unZipViewState(
    loading: Boolean.() -> Unit,
    success: T.() -> Unit,
    error: UIMessage.() -> Unit,
    empty: () -> Unit = {}
) {
    collectLatest { value: ViewState<T> ->
        when (value) {
            is ViewState.Success -> success(value.data)
            is ViewState.Error -> error(value.uiMessage)
            is ViewState.Loading -> loading(value.loading)
            ViewState.Empty -> empty()
        }
    }
}
