package com.shinedev.digitalent.view.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.shinedev.digitalent.R
import com.shinedev.digitalent.ViewModelWithPrefFactory
import com.shinedev.digitalent.data.login.LoginRequest
import com.shinedev.digitalent.data.pref.AuthPreference
import com.shinedev.digitalent.data.pref.AuthPreference.Companion.AUTH_PREFERENCE
import com.shinedev.digitalent.databinding.ActivityLoginBinding
import com.shinedev.digitalent.view.main.MainActivity
import com.shinedev.digitalent.view.register.RegisterActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = AUTH_PREFERENCE)

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupView()
        playAnimation()
        setupAction()
        observeData()
    }

    private fun setupViewModel() {
        val pref = AuthPreference.getInstance(dataStore)
        viewModel = ViewModelProvider(
            this,
            ViewModelWithPrefFactory(pref)
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
            btnLogin.setLoading(true)
            val txtEmail = edLoginEmail.getStringText()
            val txtPassword = edLoginPassword.getStringText()
            viewModel.login(LoginRequest(email = txtEmail, password = txtPassword))
        }
        tvSignup.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }
    }

    private fun observeData() {
        viewModel.result.observe(this) {
            if (!it.error) {
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(
                    this,
                    getString(R.string.text_account_error, it.message),
                    Toast.LENGTH_LONG
                ).show()
            }
        }

        viewModel.isValidInput.observe(this) {
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