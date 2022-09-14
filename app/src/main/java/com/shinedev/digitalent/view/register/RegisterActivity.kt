package com.shinedev.digitalent.view.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.shinedev.digitalent.R
import com.shinedev.digitalent.data.register.RegisterRequest
import com.shinedev.digitalent.databinding.ActivityRegisterBinding
import com.shinedev.digitalent.network.BaseResponse


class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private lateinit var viewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        playAnimation()
        setupAction()
        observeData()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            this,
            RegisterViewModelFactory()
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
            binding.btnSignup.setLoading(false)
            onRegistrationResult(result)
        }
    }

    private fun onRegistrationResult(result: BaseResponse) {
        if (!result.error) {
            Toast.makeText(this, getString(R.string.text_account_created), Toast.LENGTH_LONG).show()
            finish()
        } else {
            showSnackBar(result.message)
        }
    }

    private fun showSnackBar(errorMsg: String) {
        val snackBar = Snackbar.make(
            binding.root, getString(R.string.text_account_error, errorMsg),
            Snackbar.LENGTH_LONG
        ).setAction(getString(R.string.text_try_again), null)
        snackBar.setActionTextColor(Color.CYAN)

        val snackBarView = snackBar.view
        snackBarView.setBackgroundColor(Color.BLUE)
        val textView =
            snackBarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
        textView.setTextColor(Color.WHITE)
        snackBar.show()
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