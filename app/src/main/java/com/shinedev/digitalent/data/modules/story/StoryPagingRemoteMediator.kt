package com.shinedev.digitalent.data.modules.story

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.shinedev.digitalent.common.wrapEspressoIdlingResource
import com.shinedev.digitalent.data.local.room.dao.RemoteKeysDao
import com.shinedev.digitalent.data.local.room.dao.StoryDao
import com.shinedev.digitalent.data.local.room.entity.RemoteKeys
import com.shinedev.digitalent.data.local.room.entity.StoryEntity
import com.shinedev.digitalent.data.modules.story.model.StoryResponse

@OptIn(ExperimentalPagingApi::class)
class StoryPagingRemoteMediator(
    private val token: String,
    private val storyDao: StoryDao,
    private val remoteKeysDao: RemoteKeysDao,
    private val apiService: StoryApiService
) :
    RemoteMediator<Int, StoryResponse>() {

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, StoryResponse>
    ): MediatorResult {
        wrapEspressoIdlingResource {
            val page = when (loadType) {
                LoadType.REFRESH -> {
                    val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                    remoteKeys?.nextKey?.minus(1) ?: INITIAL_PAGE_INDEX
                }
                LoadType.PREPEND -> {
                    val remoteKeys = getRemoteKeyForFirstItem(state)
                    val prevKey = remoteKeys?.prevKey
                        ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                    prevKey
                }
                LoadType.APPEND -> {
                    val remoteKeys = getRemoteKeyForLastItem(state)
                    val nextKey = remoteKeys?.nextKey
                        ?: return MediatorResult.Success(endOfPaginationReached = remoteKeys != null)
                    nextKey
                }
            }
            try {
                var endOfPaginationReached = false
                apiService.getListStories(token, page, state.config.pageSize).onSuccess {
                    endOfPaginationReached = it.stories.isEmpty()

                    if (loadType == LoadType.REFRESH) {
                        remoteKeysDao.deleteRemoteKeys()
                        storyDao.deleteAll()
                    }
                    val prevKey = if (page == 1) null else page - 1
                    val nextKey = if (endOfPaginationReached) null else page + 1
                    val keys = it.stories.map { response ->
                        RemoteKeys(id = response.id, prevKey = prevKey, nextKey = nextKey)
                    }
                    val storyData = it.stories.map { obj ->
                        StoryEntity(
                            obj.id,
                            obj.name,
                            obj.description,
                            obj.photoUrl,
                            obj.createdAt,
                            obj.lat,
                            obj.lon
                        )
                    }
                    remoteKeysDao.insertAll(keys)
                    storyDao.inserts(storyData)
                }.onFailure {
                    return MediatorResult.Error(it)
                }
                return MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
            } catch (exception: Exception) {
                return MediatorResult.Error(exception)
            }
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, StoryResponse>): RemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { data ->
            remoteKeysDao.getRemoteKeysId(data.id)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, StoryResponse>): RemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { data ->
            remoteKeysDao.getRemoteKeysId(data.id)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, StoryResponse>): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                remoteKeysDao.getRemoteKeysId(id)
            }
        }
    }

    companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
}