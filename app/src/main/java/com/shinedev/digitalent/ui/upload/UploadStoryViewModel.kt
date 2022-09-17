package com.shinedev.digitalent.ui.upload

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.*
import com.google.android.gms.maps.model.LatLng
import com.shinedev.digitalent.data.DataResult
import com.shinedev.digitalent.data.modules.story.StoryRepository
import com.shinedev.digitalent.data.remote.BaseResponse
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class UploadStoryViewModel(private val repository: StoryRepository) : ViewModel() {

    private val _result = MutableLiveData<DataResult<BaseResponse>>()
    val result: LiveData<DataResult<BaseResponse>> = _result

    private val _uri = MutableLiveData<Uri>()
    val uri: LiveData<Uri> = _uri

    private val _bitmap = MutableLiveData<Bitmap>()
    val bitmap: LiveData<Bitmap> = _bitmap

    private val _latLng = MutableLiveData<LatLng>()
    val latLng: LiveData<LatLng> = _latLng

    private val _enableUpload = MediatorLiveData<Boolean>()
    val enableUpload: LiveData<Boolean> = _enableUpload

    init {
        _enableUpload.addSource(uri) { uri ->
            _enableUpload.value =
                checkFieldMandatory(
                    uri = uri,
                    bitmap = _bitmap.value,
                    latLng = _latLng.value
                )
        }
        _enableUpload.addSource(bitmap) { bitmap ->
            _enableUpload.value =
                checkFieldMandatory(
                    uri = _uri.value,
                    bitmap = bitmap,
                    latLng = _latLng.value
                )
        }
        _enableUpload.addSource(latLng) { latLng ->
            _enableUpload.value =
                checkFieldMandatory(
                    uri = _uri.value,
                    bitmap = _bitmap.value,
                    latLng = latLng
                )
        }
    }

    private fun checkFieldMandatory(uri: Uri?, bitmap: Bitmap?, latLng: LatLng?): Boolean =
        ((uri != null) || (bitmap != null) && latLng != null)

    fun setLatLng(latLng: LatLng) {
        _latLng.value = latLng
    }

    fun setUri(uri: Uri) {
        _uri.value = uri
    }

    fun setBitmap(bitmap: Bitmap) {
        _bitmap.value = bitmap
    }

    fun addNewStory(
        photo: MultipartBody.Part,
        description: RequestBody,
        lat: Float,
        lon: Float
    ) {
        viewModelScope.launch {
            _result.value = DataResult.Loading
            repository.addNewStory(
                photo = photo,
                description = description,
                lat = lat,
                lon = lon
            ).onSuccess {
                _result.value = DataResult.Success(it)
            }.onFailure {
                _result.value = DataResult.Error(it.localizedMessage ?: it.message.toString())
            }
        }
    }
}