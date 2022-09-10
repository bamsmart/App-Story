package com.shinedev.digitalent.view.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.shinedev.digitalent.ViewModelFactory
import com.shinedev.digitalent.databinding.ActivityRegisterBinding
import com.shinedev.digitalent.network.BaseResponse

class RegisterActivity : AppCompatActivity() {
    lateinit var binding: ActivityRegisterBinding
    lateinit var viewModel: RegisterViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel =
            ViewModelProvider(this, ViewModelFactory())[RegisterViewModel::class.java]

        viewModel.result.observe(this@RegisterActivity) { result ->
            onRegistrationResult(result)
        }

        playAnimation()
        setupAction()
    }

    private fun setupAction() = with(binding) {
        btnSignup.setOnClickListener {
            /*val name = etName.toString()
            val email = etEmail.toString()
            val password = etPassword.toString()*/
            val name = "bams"
            val email = "bamsmart.id@gmail.com"
            val password = "kodok1234"

            viewModel.signup(RegisterRequest(name = name, email = email, password = password))
        }
    }

    private fun onRegistrationResult(result: BaseResponse) {
        if (!result.error) {
            Toast.makeText(this, "Akun berhasil dibuat, silahkan login", Toast.LENGTH_LONG).show()
            finish()
        } else {
            Toast.makeText(this, "ada error nih ${result.message}", Toast.LENGTH_LONG).show()
        }
    }


    private fun playAnimation() = with(binding) {
        ObjectAnimator.ofFloat(ivLogo, View.TRANSLATION_X, -25f, 25f).apply {
            duration = 5000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(tvTitle, View.ALPHA, 1f).setDuration(500)
        val tvName =
            ObjectAnimator.ofFloat(tvName, View.ALPHA, 1f).setDuration(500)
        val fieldName =
            ObjectAnimator.ofFloat(fieldName, View.ALPHA, 1f).setDuration(500)
        val tvEmail =
            ObjectAnimator.ofFloat(tvEmail, View.ALPHA, 1f).setDuration(500)
        val fieldEmail =
            ObjectAnimator.ofFloat(fieldEmail, View.ALPHA, 1f).setDuration(500)
        val tvPassword =
            ObjectAnimator.ofFloat(tvPassword, View.ALPHA, 1f).setDuration(500)
        val fieldPassword =
            ObjectAnimator.ofFloat(fieldPassword, View.ALPHA, 1f).setDuration(500)
        val signup = ObjectAnimator.ofFloat(btnSignup, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(
                title,
                tvName,
                fieldName,
                tvEmail,
                fieldEmail,
                tvPassword,
                fieldPassword,
                signup
            )
            startDelay = 300
        }.start()
    }


}