package qiwa.gov.sa.ui.gallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import qiwa.gov.sa.base.domain.BaseViewModel
import javax.inject.Inject

@HiltViewModel
class GalleryViewModel @Inject constructor(): BaseViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is a calculator Screen"
    }
    val text: LiveData<String> = _text
}