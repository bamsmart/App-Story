package com.shinedev.digitalent.data.modules.story

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.shinedev.digitalent.common.wrapEspressoIdlingResource
import com.shinedev.digitalent.data.local.room.dao.RemoteKeysDao
import com.shinedev.digitalent.data.local.room.dao.StoryDao
import com.shinedev.digitalent.data.modules.story.model.StoryResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryRepository(
    private val token: String,
    private val storyDao: StoryDao,
    private val remoteKeysDao: RemoteKeysDao,
    private val apiService: StoryApiService
) {

    fun getPagingListStory(token: String): Flow<PagingData<StoryResponse>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(pageSize = 10),
            remoteMediator = StoryPagingRemoteMediator(
                "Bearer $token",
                storyDao,
                remoteKeysDao,
                apiService
            ),
            pagingSourceFactory = {
                storyDao.getPagedListStory()
            }
        ).flow
    }

    suspend fun getListStory(page: Int, size: Int) =
        wrapEspressoIdlingResource {
            apiService.getListStories("Bearer $token", page, size)
        }

    suspend fun addNewStory(
        photo: MultipartBody.Part,
        description: RequestBody,
        lat: Float,
        lon: Float
    ) = wrapEspressoIdlingResource {
        apiService.addNewStory(
            token = "Bearer $token",
            photo = photo,
            description = description,
            lat = lat,
            long = lon
        )
    }
}