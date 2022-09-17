package com.shinedev.digitalent.ui.register

import androidx.lifecycle.*
import com.shinedev.digitalent.data.DataResult
import com.shinedev.digitalent.data.modules.authorization.model.RegisterRequest
import com.shinedev.digitalent.data.modules.authorization.repository.UserAuthRepository
import com.shinedev.digitalent.data.remote.BaseResponse
import kotlinx.coroutines.launch

class RegisterViewModel(private val repository: UserAuthRepository) : ViewModel() {
    private val _result = MutableLiveData<DataResult<BaseResponse>>()
    val result: LiveData<DataResult<BaseResponse>> = _result

    private val _isValidName = MutableLiveData<Boolean>()
    private val isValidName: LiveData<Boolean> = _isValidName

    private val _isValidEmail = MutableLiveData<Boolean>()
    private val isValidEmail: LiveData<Boolean> = _isValidEmail

    private val _isValidPassword = MutableLiveData<Boolean>()
    private val isValidPassword: LiveData<Boolean> = _isValidPassword

    private val _isValidInput: MediatorLiveData<Boolean> = MediatorLiveData()
    val isValidInput: LiveData<Boolean> = _isValidInput

    init {
        _isValidInput.addSource(isValidName) { isValidName ->
            _isValidInput.value =
                checkFormInput(
                    isValidName = isValidName,
                    isValidEmail = _isValidEmail.value ?: false,
                    isValidPassword = _isValidPassword.value ?: false
                )
        }
        _isValidInput.addSource(isValidEmail) { isValidEmail ->
            _isValidInput.value =
                checkFormInput(
                    isValidName = _isValidName.value ?: false,
                    isValidEmail = isValidEmail,
                    isValidPassword = _isValidEmail.value ?: false
                )
        }
        _isValidInput.addSource(isValidPassword) { isValidPassword ->
            _isValidInput.value =
                checkFormInput(
                    isValidName = _isValidName.value ?: false,
                    isValidEmail = _isValidEmail.value ?: false,
                    isValidPassword = isValidPassword
                )
        }
    }

    fun signup(request: RegisterRequest) {
        viewModelScope.launch {
            _result.value = DataResult.Loading
            repository.register(request).onSuccess {
                _result.value = DataResult.Success(it)
            }.onFailure {
                _result.value = DataResult.Error(it.localizedMessage ?: it.message.toString())
            }
        }
    }

    fun isValidName(isValid: Boolean) {
        _isValidName.value = isValid
    }

    fun isValidEmail(isValid: Boolean) {
        _isValidEmail.value = isValid
    }

    fun isValidPassword(isValid: Boolean) {
        _isValidPassword.value = isValid
    }

    private fun checkFormInput(
        isValidName: Boolean,
        isValidEmail: Boolean,
        isValidPassword: Boolean
    ) =
        isValidName && isValidEmail && isValidPassword
}
