package com.shinedev.digitalent.ui.register

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.shinedev.digitalent.MainDispatcherRule
import com.shinedev.digitalent.data.DataResult
import com.shinedev.digitalent.data.modules.authorization.model.RegisterRequest
import com.shinedev.digitalent.data.modules.authorization.repository.UserAuthRepository
import com.shinedev.digitalent.data.remote.BaseResponse
import com.shinedev.digitalent.utils.getOrAwaitValue
import com.shinedev.digitalent.ui.login.AuthDataDummy
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class RegisterViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var authRepository: UserAuthRepository
    private lateinit var registerViewModel: RegisterViewModel
    private val dummyResponse = AuthDataDummy.generateRegisterResponse()
    private val registerRequest = RegisterRequest("Bams", "bamsmart.id@gmail.com", "123245")

    @Before
    fun setUp() {
        registerViewModel = RegisterViewModel(authRepository)
    }

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `when Register then return Error`() = runTest {
        val observer = Observer<DataResult<BaseResponse>> {}
        try {
            val expectedResponse = Result.failure<BaseResponse>(Throwable(""))
            Mockito.`when`(authRepository.register(registerRequest)).thenReturn(expectedResponse)

            // When
            registerViewModel.signup(registerRequest)

            val actualStories = registerViewModel.result.getOrAwaitValue()

            Mockito.verify(authRepository).register(registerRequest)
            Assert.assertTrue(actualStories is DataResult.Error)
        } finally {
            registerViewModel.result.removeObserver(observer)
        }
    }

    @Test
    fun `when Register then return Success`() = runTest {
        val observer = Observer<DataResult<BaseResponse>> {}
        try {
            val expectedResponse = Result.success(dummyResponse)
            Mockito.`when`(authRepository.register(registerRequest)).thenReturn(expectedResponse)

            // When
            registerViewModel.signup(registerRequest)

            val actualStories = registerViewModel.result.getOrAwaitValue()

            Mockito.verify(authRepository).register(registerRequest)
            Assert.assertNotNull(actualStories)
            Assert.assertTrue(actualStories is DataResult.Success)
        } finally {
            registerViewModel.result.removeObserver(observer)
        }
    }
}