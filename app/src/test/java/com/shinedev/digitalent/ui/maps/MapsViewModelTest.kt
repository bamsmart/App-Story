package com.shinedev.digitalent.ui.maps

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.shinedev.digitalent.MainDispatcherRule
import com.shinedev.digitalent.data.DataResult
import com.shinedev.digitalent.data.modules.story.StoryRepository
import com.shinedev.digitalent.data.modules.story.model.ListStoryResponse
import com.shinedev.digitalent.ui.story.StoryDataDummy
import com.shinedev.digitalent.utils.getOrAwaitValue
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

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(MockitoJUnitRunner::class)
class MapsViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()


    @Mock
    private lateinit var storyRepository: StoryRepository

    private lateinit var mapsViewModel: MapsViewModel

    @Before
    fun setup() {
        mapsViewModel = MapsViewModel(storyRepository)
    }

    @Test
    fun `when fetch data story then return Failed`() = runTest {

        val expectedResult = Result.failure<ListStoryResponse>(Throwable("unknown error"))

        `when`(storyRepository.getListStory(1, 10)).thenReturn(expectedResult)

        // When
        mapsViewModel.getListStory(1, 10)

        val actualStories = mapsViewModel.stories.getOrAwaitValue()

        Mockito.verify(storyRepository).getListStory(1, 10)
        Assert.assertTrue(actualStories is DataResult.Error)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `when fetch data story Should Not Null and return Success`() = runTest {
        val observer = Observer<DataResult<ListStoryResponse>> {}
        try {
            val dummyStories = StoryDataDummy.getDummyListStoryResponse()
            val expectedResult = Result.success(dummyStories)

            `when`(storyRepository.getListStory(1, 10)).thenReturn(expectedResult)

            // When
            mapsViewModel.getListStory(1, 10)

            val actualStories = mapsViewModel.stories.getOrAwaitValue()

            Mockito.verify(storyRepository).getListStory(1, 10)
            Assert.assertNotNull(actualStories)
            Assert.assertTrue(actualStories is DataResult.Success)
        } finally {
            mapsViewModel.stories.removeObserver(observer)
        }
    }


}