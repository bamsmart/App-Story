package com.shinedev.digitalent.ui.upload

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.shinedev.digitalent.MainDispatcherRule
import com.shinedev.digitalent.data.DataResult
import com.shinedev.digitalent.data.modules.authorization.model.LoginResponse
import com.shinedev.digitalent.data.modules.story.StoryRepository
import com.shinedev.digitalent.data.remote.BaseResponse
import com.shinedev.digitalent.ui.story.StoryDataDummy
import com.shinedev.digitalent.utils.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class UploadStoryViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var storyRepository: StoryRepository

    private lateinit var uploadStoryViewModel: UploadStoryViewModel

    @Before
    fun setup() {
        uploadStoryViewModel = UploadStoryViewModel(storyRepository)
    }

    private val dummyMultipart = StoryDataDummy.generateDummyMultipartFile()
    private val dummyDescription = StoryDataDummy.generateDummyRequestBody()


    @Test
    fun `when add story then failed`() = runTest {
        val observer = Observer<DataResult<BaseResponse>> {}
        try {
            val expectedResponse = Result.failure<LoginResponse>(Throwable("Unknown error"))
            Mockito.`when`(
                storyRepository.addNewStory(
                    dummyMultipart,
                    dummyDescription,
                    0.3f,
                    0.0f
                )
            ).thenReturn(expectedResponse)

            // When
            uploadStoryViewModel.addNewStory(
                dummyMultipart,
                dummyDescription,
                0.3f,
                0.0f
            )

            val actualStories = uploadStoryViewModel.result.getOrAwaitValue()

            Mockito.verify(storyRepository).addNewStory(
                dummyMultipart,
                dummyDescription,
                0.3f,
                0.0f
            )
            assertTrue(actualStories is DataResult.Error)
        } finally {
            uploadStoryViewModel.result.removeObserver(observer)
        }
    }

    @Test
    fun `when add story then success`() = runTest {
        val observer = Observer<DataResult<BaseResponse>> {}
        try {
            val dummyResponse = StoryDataDummy.generateBaseResponse()
            val expectedResponse = Result.success(dummyResponse)
            Mockito.`when`(
                storyRepository.addNewStory(
                    dummyMultipart,
                    dummyDescription,
                    0.3f,
                    0.0f
                )
            ).thenReturn(expectedResponse)

            // When
            uploadStoryViewModel.addNewStory(
                dummyMultipart,
                dummyDescription,
                0.3f,
                0.0f
            )

            val actualStories = uploadStoryViewModel.result.getOrAwaitValue()

            Mockito.verify(storyRepository).addNewStory(
                dummyMultipart,
                dummyDescription,
                0.3f,
                0.0f
            )
            assertTrue(actualStories is DataResult.Success)
        } finally {
            uploadStoryViewModel.result.removeObserver(observer)
        }
    }
}