package com.shinedev.digitalent.ui.login

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.shinedev.digitalent.MainDispatcherRule
import com.shinedev.digitalent.data.DataResult
import com.shinedev.digitalent.data.modules.authorization.model.LoginRequest
import com.shinedev.digitalent.data.modules.authorization.model.LoginResponse
import com.shinedev.digitalent.data.modules.authorization.repository.UserAuthRepository
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

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var authRepository: UserAuthRepository
    private lateinit var loginViewModel: LoginViewModel
    private val dummyResponse = AuthDataDummy.generateLoginResponse()
    private val loginRequest = LoginRequest("bamsmart.id@gmail.com", "123245")

    @Before
    fun setUp() {
        loginViewModel = LoginViewModel(authRepository)
    }

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `when Login then return Failed`() = runTest {
        val observer = Observer<DataResult<LoginResponse>> {}
        try {
            val expectedResponse = Result.failure<LoginResponse>(Throwable("Unknown error"))
            `when`(authRepository.login(loginRequest)).thenReturn(expectedResponse)

            // When
            loginViewModel.login(loginRequest)

            val actualStories = loginViewModel.result.getOrAwaitValue()

            Mockito.verify(authRepository).login(loginRequest)
            Assert.assertTrue(actualStories is DataResult.Error)
        } finally {
            loginViewModel.result.removeObserver(observer)
        }
    }

    @Test
    fun `when Login return Success`() = runTest {
        val observer = Observer<DataResult<LoginResponse>> {}
        try {
            val expectedResponse = Result.success(dummyResponse)
            `when`(authRepository.login(loginRequest)).thenReturn(expectedResponse)

            // When
            loginViewModel.login(loginRequest)

            val actualStories = loginViewModel.result.getOrAwaitValue()

            Mockito.verify(authRepository).login(loginRequest)
            Assert.assertNotNull(actualStories)
            Assert.assertTrue(actualStories is DataResult.Success)
        } finally {
            loginViewModel.result.removeObserver(observer)
        }
    }
}