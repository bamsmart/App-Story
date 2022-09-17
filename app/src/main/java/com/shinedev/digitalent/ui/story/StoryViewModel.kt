package com.shinedev.digitalent.ui.story

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.shinedev.digitalent.data.local.pref.AuthPreferenceDataStore
import com.shinedev.digitalent.data.modules.story.StoryRepository
import com.shinedev.digitalent.data.modules.story.model.StoryResponse
import kotlinx.coroutines.launch

class StoryViewModel(
    private val pref: AuthPreferenceDataStore, private val repository: StoryRepository
) : ViewModel() {

    fun getAllStories(token: String): LiveData<PagingData<StoryResponse>> =
        repository.getPagingListStory(token).cachedIn(viewModelScope).asLiveData()

    fun logout() {
        viewModelScope.launch {
            pref.clear()
        }
    }
}