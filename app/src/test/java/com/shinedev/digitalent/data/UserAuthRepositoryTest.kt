package com.shinedev.digitalent.data

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.shinedev.digitalent.MainDispatcherRule
import com.shinedev.digitalent.data.local.pref.AuthPreferenceDataStore
import com.shinedev.digitalent.data.modules.authorization.model.LoginRequest
import com.shinedev.digitalent.data.modules.authorization.model.LoginResponse
import com.shinedev.digitalent.data.modules.authorization.model.RegisterRequest
import com.shinedev.digitalent.data.modules.authorization.repository.UserAuthApiService
import com.shinedev.digitalent.data.modules.authorization.repository.UserAuthRepository
import com.shinedev.digitalent.data.remote.BaseResponse
import com.shinedev.digitalent.ui.story.StoryDataDummy
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class UserAuthRepositoryTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Mock
    private lateinit var pref: AuthPreferenceDataStore

    @Mock
    private lateinit var apiService: UserAuthApiService

    private lateinit var authRepository: UserAuthRepository

    @Before
    fun setUp() {
        authRepository = UserAuthRepository(pref, apiService)
    }

    @Test
    fun `when login then return failed`() = runTest {
        val expectedResponse = Result.failure<LoginResponse>(Throwable(""))

        val request = LoginRequest("bamsmart.idx@gmail.com", "1234567")
        `when`(
            apiService.login(request)
        ).thenReturn(expectedResponse)

        val actualResponse = apiService.login(request)

        verify(apiService)
            .login(request)

        assertNotNull(actualResponse)
        assertEquals(expectedResponse, actualResponse)
        assertTrue(actualResponse.isFailure)
    }

    @Test
    fun `when login then return success`() = runTest {
        val dummyResponse = StoryDataDummy.generateLoginResponse()
        val expectedResponse = Result.success(dummyResponse)

        val request = LoginRequest("bamsmart.idx@gmail.com", "1234567")
        `when`(
            apiService.login(request)
        ).thenReturn(expectedResponse)

        val actualResponse = apiService.login(request)

        verify(apiService)
            .login(request)

        assertNotNull(actualResponse)
        assertEquals(expectedResponse, actualResponse)
        assertTrue(actualResponse.isSuccess)
    }


    @Test
    fun `when register then return failed`() = runTest {
        val expectedResponse = Result.failure<BaseResponse>(Throwable(""))

        val request = RegisterRequest("Bams", "bamsmart.idx@gmail.com", "1234567")
        `when`(
            apiService.register(request)
        ).thenReturn(expectedResponse)

        val actualResponse = apiService.register(request)

        verify(apiService)
            .register(request)

        assertNotNull(actualResponse)
        assertEquals(expectedResponse, actualResponse)
        assertTrue(actualResponse.isFailure)
    }

    @Test
    fun `when register then return success`() = runTest {
        val dummyResponse = StoryDataDummy.generateBaseResponse()
        val expectedResponse = Result.success(dummyResponse)

        val request = RegisterRequest("Bams", "bamsmart.idx@gmail.com", "1234567")
        `when`(
            apiService.register(request)
        ).thenReturn(expectedResponse)

        val actualResponse = apiService.register(request)

        verify(apiService)
            .register(request)

        assertNotNull(actualResponse)
        assertEquals(expectedResponse, actualResponse)
        assertTrue(actualResponse.isSuccess)
    }
}