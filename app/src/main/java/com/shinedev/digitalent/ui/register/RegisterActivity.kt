package com.shinedev.digitalent.ui.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.shinedev.digitalent.R
import com.shinedev.digitalent.ViewModelFactory
import com.shinedev.digitalent.common.animateVisibility
import com.shinedev.digitalent.data.DataResult
import com.shinedev.digitalent.data.modules.authorization.model.RegisterRequest
import com.shinedev.digitalent.databinding.ActivityRegisterBinding
import com.shinedev.digitalent.databinding.SnackbarSuccessBinding
import com.shinedev.digitalent.databinding.SnackbarWarningBinding
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModel: RegisterViewModel

    private lateinit var warningBinding: SnackbarWarningBinding
    private lateinit var successBinding: SnackbarSuccessBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        warningBinding = SnackbarWarningBinding.bind(binding.root)
        successBinding = SnackbarSuccessBinding.bind(binding.root)
        setContentView(binding.root)

        warningBinding.snackbarSection.visibility = View.GONE
        successBinding.snackbarSuccessSection.visibility = View.GONE

        setupViewModel()
        playAnimation()
        setupAction()
        observeData()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(this@RegisterActivity)
        )[RegisterViewModel::class.java]
    }

    private fun setupAction() = with(binding) {
        edRegisterName.textChangedListener(lifecycleScope) {
            viewModel.isValidName(edRegisterName.getIsValidValue())
        }

        edRegisterEmail.textChangedListener(lifecycleScope) {
            viewModel.isValidEmail(edRegisterEmail.getIsValidValue())
        }

        edRegisterPassword.textChangedListener(lifecycleScope) {
            viewModel.isValidPassword(edRegisterPassword.getIsValidValue())
        }

        btnSignup.apply {
            setOnClickListener {
                setLoading(true)
                val txtName = edRegisterName.getStringText()
                val txtEmail = edRegisterEmail.getStringText()
                val txtPassword = edRegisterPassword.getStringText()

                viewModel.signup(
                    RegisterRequest(
                        name = txtName,
                        email = txtEmail,
                        password = txtPassword
                    )
                )
            }
        }
    }

    private fun observeData() {
        viewModel.isValidInput.observe(this) {
            binding.btnSignup.isEnabled = it
        }
        viewModel.result.observe(this@RegisterActivity) { result ->
            if (result != null) {
                when (result) {
                    is DataResult.Loading -> {
                        binding.btnSignup.setLoading(true)
                    }
                    is DataResult.Success -> {
                        binding.btnSignup.setLoading(false)
                        with(successBinding) {
                            snackbarSuccessSection.visibility = View.VISIBLE
                            tvSnackbarSuccess.text =
                                getString(R.string.text_account_created)
                            ivSnackbar.setOnClickListener {
                                snackbarSuccessSection.animateVisibility(false)
                                lifecycleScope.launch {
                                    delay(3000L)
                                    finish()
                                }
                            }
                        }
                    }
                    is DataResult.Error -> {
                        binding.btnSignup.setLoading(false)
                        with(warningBinding) {
                            snackbarSection.visibility = View.VISIBLE
                            tvSnackbarWarning.text =
                                getString(R.string.text_account_error)
                            ivSnackbarWarningClose.setOnClickListener {
                                snackbarSection.visibility = View.GONE
                            }
                        }
                    }
                }
            }
        }
    }

    private fun playAnimation() = with(binding) {
        ObjectAnimator.ofFloat(ivLogo, View.TRANSLATION_X, -25f, 25f).apply {
            duration = 5000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(tvTitle, View.ALPHA, 1f).setDuration(300)
        val fieldName =
            ObjectAnimator.ofFloat(edRegisterName, View.ALPHA, 1f).setDuration(300)
        val fieldEmail =
            ObjectAnimator.ofFloat(edRegisterEmail, View.ALPHA, 1f).setDuration(300)
        val fieldPassword =
            ObjectAnimator.ofFloat(edRegisterPassword, View.ALPHA, 1f).setDuration(300)
        val signup = ObjectAnimator.ofFloat(btnSignup, View.ALPHA, 1f).setDuration(300)

        AnimatorSet().apply {
            playSequentially(
                title,
                fieldName,
                fieldEmail,
                fieldPassword,
                signup
            )
            startDelay = 300
        }.start()
    }


}