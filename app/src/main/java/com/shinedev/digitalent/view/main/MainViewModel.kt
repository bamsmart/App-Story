package com.shinedev.digitalent.view.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shinedev.digitalent.network.NetworkBuilder
import com.shinedev.digitalent.pref.AuthPreference
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val pref: AuthPreference) : ViewModel() {
    private val storyService = NetworkBuilder.createService(StoryService::class.java)

    private val _listStory = MutableLiveData<List<StoryResponse>>()
    val listStory: LiveData<List<StoryResponse>> = _listStory

    fun getListStory() {
        viewModelScope.launch {
            pref.getToken().catch { e ->
                e.printStackTrace()
            }.collect { token ->
                val client = storyService.getListStories("Bearer $token", page = 1, 100)
                client.enqueue(object : Callback<ListStoryResponse> {
                    override fun onResponse(
                        call: Call<ListStoryResponse>,
                        response: Response<ListStoryResponse>
                    ) {
                        if (response.isSuccessful) {
                            _listStory.value = response.body()?.stories
                        } else {
                            Log.e(TAG, "onFailure: ${response.message()}")
                        }
                    }

                    override fun onFailure(call: Call<ListStoryResponse>, t: Throwable) {
                        Log.e(TAG, "onFailure: ${t.message.toString()}")
                    }
                })
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            pref.clear()
        }
    }

    companion object {
        private const val TAG = "MainViewModel"
    }
}