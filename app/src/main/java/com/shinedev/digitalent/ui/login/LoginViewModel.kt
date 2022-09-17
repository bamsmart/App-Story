package com.shinedev.digitalent.ui.login

import androidx.lifecycle.*
import com.shinedev.digitalent.data.DataResult
import com.shinedev.digitalent.data.modules.authorization.model.LoginRequest
import com.shinedev.digitalent.data.modules.authorization.model.LoginResponse
import com.shinedev.digitalent.data.modules.authorization.repository.UserAuthRepository
import kotlinx.coroutines.launch


class LoginViewModel(private val repository: UserAuthRepository) : ViewModel() {

    private val _result = MutableLiveData<DataResult<LoginResponse>>()
    val result: LiveData<DataResult<LoginResponse>> = _result

    private val _isValidEmail = MutableLiveData<Boolean>()
    private val isValidEmail: LiveData<Boolean> = _isValidEmail

    private val _isValidPassword = MutableLiveData<Boolean>()
    private val isValidPassword: LiveData<Boolean> = _isValidPassword

    private val _isValidInput: MediatorLiveData<Boolean> = MediatorLiveData()
    val isValidInput: LiveData<Boolean> = _isValidInput

    init {
        _isValidInput.addSource(isValidEmail) { isValidEmail ->
            _isValidInput.value =
                checkFormInput(
                    isValidEmail = isValidEmail,
                    isValidPassword = isValidPassword.value ?: false
                )
        }
        _isValidInput.addSource(isValidPassword) { isValidPassword ->
            _isValidInput.value =
                checkFormInput(
                    isValidEmail = isValidEmail.value ?: false,
                    isValidPassword = isValidPassword
                )
        }
    }

    fun login(request: LoginRequest) {
        viewModelScope.launch {
            _result.value = DataResult.Loading
            repository.apply {
                login(request).onSuccess {
                    _result.value = DataResult.Success(it)
                    setIsLogin(true)
                    setToken(it.result?.token ?: "")
                }.onFailure {
                    _result.value = DataResult.Error(it.localizedMessage ?: it.message.toString())
                }
            }
        }
    }

    fun isValidEmail(isValid: Boolean) {
        _isValidEmail.value = isValid
    }

    fun isValidPassword(isValid: Boolean) {
        _isValidPassword.value = isValid
    }

    private fun checkFormInput(isValidEmail: Boolean, isValidPassword: Boolean) =
        isValidEmail && isValidPassword
}