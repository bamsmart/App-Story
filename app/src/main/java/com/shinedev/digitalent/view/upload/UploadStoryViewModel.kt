package com.shinedev.digitalent.view.upload

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.*
import com.shinedev.digitalent.network.BaseResponse
import com.shinedev.digitalent.network.NetworkBuilder
import com.shinedev.digitalent.data.pref.AuthPreference
import com.shinedev.digitalent.domain.story.StoryApiService
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UploadStoryViewModel(private val pref: AuthPreference) : ViewModel() {
    private val storyService = NetworkBuilder.createService(StoryApiService::class.java)

    private val _result = MutableLiveData<BaseResponse>()
    val result: LiveData<BaseResponse> = _result

    private val _uri = MutableLiveData<Uri>()
    val uri: LiveData<Uri> = _uri

    private val _bitmap = MutableLiveData<Bitmap>()
    val bitmap: LiveData<Bitmap> = _bitmap

    private val _enableUpload = MediatorLiveData<Boolean>()
    val enableUpload: LiveData<Boolean> = _enableUpload

    init {
        _enableUpload.addSource(uri) { uri ->
            _enableUpload.value =
                checkUriOrBitmap(
                    uri = uri,
                    bitmap = _bitmap.value
                )
        }
        _enableUpload.addSource(bitmap) { bitmap ->
            _enableUpload.value =
                checkUriOrBitmap(
                    uri = _uri.value,
                    bitmap = bitmap
                )
        }
    }

    private fun checkUriOrBitmap(uri: Uri?, bitmap: Bitmap?): Boolean =
        (uri != null) || (bitmap != null)

    fun setUri(uri: Uri) {
        _uri.value = uri
    }

    fun setBitmap(bitmap: Bitmap) {
        _bitmap.value = bitmap
    }

    fun addNewStory(
        photo: MultipartBody.Part,
        description: RequestBody,
        lat: Double,
        lon: Double
    ) {
        viewModelScope.launch {
            pref.getToken().catch { e ->
                e.printStackTrace()
            }.collect { token ->
                val service = storyService.addNewStory(
                    token = "Bearer $token",
                    photo = photo,
                    description = description,
                    lat = lat,
                    long = lon
                )

                service.enqueue(object : Callback<BaseResponse> {
                    override fun onResponse(
                        call: Call<BaseResponse>,
                        response: Response<BaseResponse>
                    ) {
                        if (response.isSuccessful) {
                            val responseBody = response.body()
                            if (responseBody != null && !responseBody.error) {
                                _result.value = response.body()
                            }
                        }
                    }

                    override fun onFailure(call: Call<BaseResponse>, t: Throwable) {

                    }
                })
            }
        }
    }
}