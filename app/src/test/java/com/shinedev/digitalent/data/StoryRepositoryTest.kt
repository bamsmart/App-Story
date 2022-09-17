package com.shinedev.digitalent.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.*
import com.shinedev.digitalent.MainDispatcherRule
import com.shinedev.digitalent.data.local.room.dao.RemoteKeysDao
import com.shinedev.digitalent.data.local.room.dao.StoryDao
import com.shinedev.digitalent.data.modules.story.StoryApiService
import com.shinedev.digitalent.data.modules.story.StoryPagingRemoteMediator
import com.shinedev.digitalent.data.modules.story.StoryRepository
import com.shinedev.digitalent.data.modules.story.model.ListStoryResponse
import com.shinedev.digitalent.data.modules.story.model.StoryResponse
import com.shinedev.digitalent.data.remote.BaseResponse
import com.shinedev.digitalent.ui.story.StoryDataDummy
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner


@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class StoryRepositoryTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var storyDao: StoryDao

    @Mock
    private lateinit var remoteKeyDao: RemoteKeysDao

    @Mock
    private lateinit var apiService: StoryApiService

    private lateinit var storyRepository: StoryRepository

    private val token = "tokenKey"
    private val dummyMultipart = StoryDataDummy.generateDummyMultipartFile()
    private val dummyDescription = StoryDataDummy.generateDummyRequestBody()

    @Before
    fun setUp() {
        storyRepository = StoryRepository(token, storyDao, remoteKeyDao, apiService)
    }

    @Test
    fun `when getStory then failed`() = runTest {
        val expectedStory = Result.failure<ListStoryResponse>(Throwable(""))

        `when`(apiService.getListStories("apiKey", 1, 5)).thenReturn(expectedStory)

        val actualStory = apiService.getListStories("apiKey", 1, 5)

        Assert.assertNotNull(actualStory)
        Assert.assertEquals(expectedStory, actualStory)
        Assert.assertTrue(actualStory.isFailure)
    }

    @Test
    fun `when getStory Should Not Null and success`() = runTest {
        val expectedStory = Result.success(StoryDataDummy.getDummyListStoryResponse())

        `when`(apiService.getListStories("apiKey", 1, 5)).thenReturn(expectedStory)

        val actualStory = apiService.getListStories("apiKey", 1, 5)

        Assert.assertNotNull(actualStory)
        Assert.assertTrue(actualStory.isSuccess)
        Assert.assertEquals(expectedStory, actualStory)
    }

    @OptIn(ExperimentalPagingApi::class)
    @Test
    fun `when getPageListStory Should Not Null`() = runTest {
        val tokenKey = "tokenKeyApi"
        storyDao = FakeStoryDao()
        remoteKeyDao = FakeRemoteKeyDao()
        apiService = FakeStoryApiService()
        storyRepository = StoryRepository(tokenKey, storyDao, remoteKeyDao, apiService)

        val remoteMediator = StoryPagingRemoteMediator(
            token, storyDao, remoteKeyDao, apiService
        )
        val pagingState = PagingState<Int, StoryResponse>(
            listOf(), null, PagingConfig(10), 10
        )

        val result = remoteMediator.load(LoadType.REFRESH, pagingState)

        Assert.assertTrue(result is RemoteMediator.MediatorResult.Success)
        Assert.assertFalse((result as RemoteMediator.MediatorResult.Success).endOfPaginationReached)
    }

    @Test
    fun `when post new story then return failed`() = runTest {
        val expectedResponse = Result.failure<BaseResponse>(Throwable(""))
        `when`(
            apiService.addNewStory(
                token,
                dummyMultipart,
                dummyDescription,
                0.3f,
                0.0f
            )
        ).thenReturn(expectedResponse)

        val actualResponse = apiService.addNewStory(
            token,
            dummyMultipart,
            dummyDescription,
            0.3f,
            0.0f
        )

        Mockito.verify(apiService)
            .addNewStory(
                token,
                dummyMultipart,
                dummyDescription,
                0.3f,
                0.0f
            )

        Assert.assertNotNull(actualResponse)
        Assert.assertEquals(expectedResponse, actualResponse)
        Assert.assertTrue(actualResponse.isFailure)
    }

    @Test
    fun `when post new story then return success`() = runTest {
        val dummyResponse = StoryDataDummy.generateBaseResponse()
        val expectedResponse = Result.success(dummyResponse)
        `when`(
            apiService.addNewStory(
                token,
                dummyMultipart,
                dummyDescription,
                0.3f,
                0.0f
            )
        ).thenReturn(expectedResponse)

        val actualResponse = apiService.addNewStory(
            token,
            dummyMultipart,
            dummyDescription,
            0.3f,
            0.0f
        )


        Mockito.verify(apiService)
            .addNewStory(
                token,
                dummyMultipart,
                dummyDescription,
                0.3f,
                0.0f
            )

        Assert.assertNotNull(actualResponse)
        Assert.assertEquals(expectedResponse, actualResponse)
        Assert.assertTrue(actualResponse.isSuccess)
    }
}

