package com.shinedev.digitalent.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.shinedev.digitalent.R
import com.shinedev.digitalent.ViewModelFactory
import com.shinedev.digitalent.data.DataResult
import com.shinedev.digitalent.data.modules.authorization.model.LoginRequest
import com.shinedev.digitalent.databinding.ActivityLoginBinding
import com.shinedev.digitalent.databinding.SnackbarWarningBinding
import com.shinedev.digitalent.ui.main.MainActivity
import com.shinedev.digitalent.ui.main.MainActivity.Companion.TOKEN
import com.shinedev.digitalent.ui.register.RegisterActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var snackBarBinding: SnackbarWarningBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        snackBarBinding = SnackbarWarningBinding.bind(binding.rootLayout)
        setContentView(binding.root)

        snackBarBinding.snackbarSection.visibility = View.GONE

        setupViewModel()
        setupView()
        playAnimation()
        setupAction()
        observeData()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this,
            ViewModelFactory(context = this@LoginActivity)
        )[LoginViewModel::class.java]
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun setupAction() = with(binding) {
        edLoginEmail.textChangedListener(lifecycleScope) {
            viewModel.isValidEmail(edLoginEmail.getIsValidValue())
        }
        edLoginPassword.textChangedListener(lifecycleScope) {
            viewModel.isValidPassword(edLoginPassword.getIsValidValue())
        }

        btnLogin.setOnClickListener {
            val txtEmail = edLoginEmail.getStringText()
            val txtPassword = edLoginPassword.getStringText()
            viewModel.login(LoginRequest(email = txtEmail, password = txtPassword))
        }
        tvSignup.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }
    }

    private fun observeData() = with(binding) {
        viewModel.result.observe(this@LoginActivity) { result ->
            when (result) {
                is DataResult.Loading -> {
                    btnLogin.setLoading(true)
                }
                is DataResult.Success -> {
                    btnLogin.setLoading(false)
                    startActivity(
                        Intent(this@LoginActivity, MainActivity::class.java).putExtra(
                            TOKEN,
                            result.data.result?.token
                        )
                    )
                    finish()
                }
                is DataResult.Error -> {
                    btnLogin.setLoading(false)
                    with(snackBarBinding) {
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

        viewModel.isValidInput.observe(this@LoginActivity) {
            binding.btnLogin.isEnabled = it
        }
    }


    private fun playAnimation() = with(binding) {
        ObjectAnimator.ofFloat(ivLogo, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(tvTitle, View.ALPHA, 1f).setDuration(500)
        val message = ObjectAnimator.ofFloat(tvMessage, View.ALPHA, 1f).setDuration(500)
        val login = ObjectAnimator.ofFloat(btnLogin, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(
                title,
                message,
                login
            )
            startDelay = 500
        }.start()
    }
}