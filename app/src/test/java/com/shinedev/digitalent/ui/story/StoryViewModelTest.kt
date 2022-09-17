package com.shinedev.digitalent.ui.story

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.AsyncPagingDataDiffer
import androidx.recyclerview.widget.ListUpdateCallback
import com.shinedev.digitalent.MainDispatcherRule
import com.shinedev.digitalent.data.local.pref.AuthPreferenceDataStore
import com.shinedev.digitalent.data.modules.story.StoryRepository
import com.shinedev.digitalent.ui.main.adapter.StoryAdapter
import com.shinedev.digitalent.utils.FakePagingDataSource
import com.shinedev.digitalent.utils.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class StoryViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var storyRepository: StoryRepository

    @Mock
    lateinit var pref: AuthPreferenceDataStore

    private lateinit var storyViewModel: StoryViewModel

    @Before
    fun setup() {
        storyViewModel = StoryViewModel(pref, storyRepository)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `when fetch data story then return failed`() = runTest {
        val data = FakePagingDataSource.snapshot(listOf())
        val token = "tokenKey"
        `when`(storyRepository.getPagingListStory(token)).thenReturn(flowOf(data))

        val actualStories = storyViewModel.getAllStories(token).getOrAwaitValue()
        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = listUpdateCallback,
            mainDispatcher = mainDispatcherRule.testDispatcher,
            workerDispatcher = mainDispatcherRule.testDispatcher
        )
        differ.submitData(actualStories)

        advanceUntilIdle()

        verify(storyRepository).getPagingListStory(token)
        assertNotNull(differ.snapshot())
        assertEquals(0, differ.snapshot().size)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `when fetch data story Should Not Null and Return Success`() = runTest {
        val dummyStories = StoryDataDummy.generateDummyStoryResponse()
        val data = FakePagingDataSource.snapshot(dummyStories)
        val token = "tokenKey"

        `when`(storyRepository.getPagingListStory(token)).thenReturn(flowOf(data))

        val actualStories = storyViewModel.getAllStories(token).getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DIFF_CALLBACK,
            updateCallback = listUpdateCallback,
            mainDispatcher = mainDispatcherRule.testDispatcher,
            workerDispatcher = mainDispatcherRule.testDispatcher
        )
        differ.submitData(actualStories)

        advanceUntilIdle()

        verify(storyRepository).getPagingListStory(token)
        assertNotNull(differ.snapshot())
        assertEquals(dummyStories.size, differ.snapshot().size)
    }

    private val listUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }
}