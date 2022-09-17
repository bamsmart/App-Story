package com.shinedev.digitalent.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.shinedev.digitalent.DataDummy
import com.shinedev.digitalent.data.local.room.StoryDatabase
import com.shinedev.digitalent.data.local.room.dao.StoryDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.*
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class StoryDaoTest {
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: StoryDatabase
    private lateinit var dao: StoryDao

    private val sampleListStory = DataDummy.generateDummyStoryEntity()
    private val sampleStory = DataDummy.generateDummyStoryEntity()[0]

    @Before
    fun initDb() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            StoryDatabase::class.java
        ).build()
        dao = database.storyDao()
    }

    @After
    fun closeDb() = database.close()

    @Test
    fun saveStory() = runTest {
        dao.inserts(sampleListStory)
        val actualStory = dao.getAllStory()
        Assert.assertEquals(sampleStory.name, actualStory[0].name)
    }

    @Test
    fun deleteStory() = runTest {
        dao.deleteAll()
        val actualStory = dao.getAllStory()
        Assert.assertTrue(actualStory.isEmpty())
    }
}