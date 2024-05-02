package ballpark.buddy.android.ui.feedback

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import androidx.appcompat.widget.AppCompatTextView
import ballpark.buddy.android.resources.StringsResourceManager
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.lifecycle.HiltViewModel
import ballpark.buddy.android.R
import ballpark.buddy.android.base.domain.BaseViewModel
import ballpark.buddy.android.editText.CustomEditTextField
import ballpark.buddy.android.extentions.default
import ballpark.buddy.android.extentions.inverse
import ballpark.buddy.android.extentions.visibility
import javax.inject.Inject

@HiltViewModel
class FeedbackViewModel @Inject constructor(
    private val stringsResourceManager: StringsResourceManager
): BaseViewModel() {

   fun onTapOfSendButton(idField: CustomEditTextField,email:CustomEditTextField,messageField: TextInputLayout,errorMessage: AppCompatTextView){
       if (idField.isValid().inverse) {
           idField.setError(stringsResourceManager.getString(R.string.iqamaValidation), idField)
           return
       }
       if (idField.getFieldText().length != 10 ){
           idField.setError(stringsResourceManager.getString(R.string.for10Digits), idField)
           return
       }
       if (email.isValid().inverse){
           email.setError(stringsResourceManager.getString(R.string.emailValidation), email)
           return
       }
       if (messageField.editText?.text?.isEmpty().default){
           errorMessage.visibility(true)
           setError(messageField)
           return
       }
       else{
           // Api Call
       }
   }

    private fun setError(editTextField: TextInputLayout) {
        shake(editTextField).setDuration(800).start()
    }

    private fun shake(view: View): AnimatorSet {
        val animatorSet = AnimatorSet()
        val object1: ObjectAnimator = ObjectAnimator.ofFloat(
            view,
            "translationX",
            0f,
            25f,
            -25f,
            25f,
            -25f,
            15f,
            -15f,
            6f,
            -6f,
            0f
        )
        animatorSet.playSequentially(object1)
        return animatorSet
    }


}