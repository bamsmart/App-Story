package com.shinedev.digitalent.view.story

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shinedev.digitalent.network.BaseResponse
import com.shinedev.digitalent.network.NetworkBuilder
import com.shinedev.digitalent.pref.AuthPreference
import com.shinedev.digitalent.view.main.StoryService
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UploadStoryViewModel(private val pref: AuthPreference) : ViewModel() {
    private val storyService = NetworkBuilder.createService(StoryService::class.java)

    private val _result = MutableLiveData<BaseResponse>()
    val result: LiveData<BaseResponse> = _result

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
                        } else {

                        }
                    }

                    override fun onFailure(call: Call<BaseResponse>, t: Throwable) {

                    }
                })
            }
        }
    }
}