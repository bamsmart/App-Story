package com.shinedev.digitalent.view.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
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
import com.shinedev.digitalent.ViewModelWithPrefFactory
import com.shinedev.digitalent.databinding.ActivityLoginBinding
import com.shinedev.digitalent.pref.AuthPreference
import com.shinedev.digitalent.pref.AuthPreference.Companion.AUTH_PREFERENCE
import com.shinedev.digitalent.view.login.service.LoginRequest
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

        val pref = AuthPreference.getInstance(dataStore)
        viewModel =
            ViewModelProvider(this, ViewModelWithPrefFactory(pref))[LoginViewModel::class.java]

        setupView()
        playAnimation()
        setupAction()
        observeData()
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
            viewModel.login(LoginRequest(email = "bamsmart.id@gmail.com", password = "kodok1234"))
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
                Toast.makeText(this, "ada error nih ${it.message}", Toast.LENGTH_LONG).show()
            }
        }

        viewModel.isValidInput.observe(this) {
            Log.d("LoginActivity", "observe input $it")
            binding.btnLogin.isEnabled = it
        }


    }


    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.ivLogo, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(binding.tvTitle, View.ALPHA, 1f).setDuration(500)
        val message = ObjectAnimator.ofFloat(binding.tvMessage, View.ALPHA, 1f).setDuration(500)
        /*val emailTextView =
            ObjectAnimator.ofFloat(binding.emailTextView, View.ALPHA, 1f).setDuration(500)
        val emailEditTextLayout =
            ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(500)*/
       /* val passwordTextView =
            ObjectAnimator.ofFloat(binding.passwordTextView, View.ALPHA, 1f).setDuration(500)
        val passwordEditTextLayout =
            ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(500)*/
        val login = ObjectAnimator.ofFloat(binding.btnLogin, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(
                title,
                message,
                /* emailTextView,
                 emailEditTextLayout,*/
             /*   passwordTextView,
                passwordEditTextLayout,*/
                login
            )
            startDelay = 500
        }.start()
    }
}