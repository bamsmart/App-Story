package com.shinedev.digitalent.view.main

import androidx.lifecycle.*
import com.shinedev.digitalent.database.entity.StoryEntity
import com.shinedev.digitalent.network.NetworkBuilder
import com.shinedev.digitalent.pref.AuthPreference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val pref: AuthPreference, private val repository: StoryRepository) :
    ViewModel() {

    private val storyService = NetworkBuilder.createService(StoryService::class.java)

    private val _listStory = MutableLiveData<List<StoryResponse>>()
    val listStory: LiveData<List<StoryResponse>> = _listStory

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getListStory() {
        viewModelScope.launch {
            pref.getToken().catch { e ->
                e.printStackTrace()
            }.collect { token ->
                _isLoading.value = true
                val client = storyService.getListStories("Bearer $token", page = 1, 100)
                client.enqueue(object : Callback<ListStoryResponse> {
                    override fun onResponse(
                        call: Call<ListStoryResponse>,
                        response: Response<ListStoryResponse>
                    ) {
                        if (response.isSuccessful) {
                            _listStory.value = response.body()?.stories
                        } else {
                            _listStory.value = listOf()
                        }
                        _isLoading.value = false
                    }

                    override fun onFailure(call: Call<ListStoryResponse>, t: Throwable) {
                        _listStory.value = listOf()
                        _isLoading.value = false
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

    fun insetUpdatedListStory(list: List<StoryEntity>) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAll()
            repository.inserts(list)
        }
    }
}

class MainViewModelFactory(
    private val pref: AuthPreference,
    private val repository: StoryRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(pref, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}