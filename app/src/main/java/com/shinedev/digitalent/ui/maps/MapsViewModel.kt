package com.shinedev.digitalent.ui.maps

import android.content.Context
import androidx.lifecycle.*
import com.shinedev.digitalent.data.DataResult
import com.shinedev.digitalent.data.modules.story.model.ListStoryResponse
import com.shinedev.digitalent.di.Injection
import com.shinedev.digitalent.data.modules.story.StoryRepository
import kotlinx.coroutines.launch

class MapsViewModel(private val repository: StoryRepository) :
    ViewModel() {

    private val _stories = MutableLiveData<DataResult<ListStoryResponse>>()
    val stories: LiveData<DataResult<ListStoryResponse>> = _stories

    fun getListStory(page: Int, size: Int) {
        viewModelScope.launch {
            _stories.value = DataResult.Loading
            repository.getListStory(page, size).onSuccess {
                _stories.value = DataResult.Success(it)
            }.onFailure {
                _stories.value = DataResult.Error(it.localizedMessage ?: it.message.toString())
            }
        }
    }
}

class MapsViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MapsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MapsViewModel(Injection.provideStoryRepository(context)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}