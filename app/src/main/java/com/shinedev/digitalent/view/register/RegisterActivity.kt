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

        playAnimation()
        setupAction()
        observeData()
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
                /*val name = etName.toString()
                val email = etEmail.toString()
                val password = etPassword.toString()*/
                val name = "bams"
                val email = "bamsmart.id@gmail.com"
                val password = "kodok1234"
                viewModel.signup(RegisterRequest(name = name, email = email, password = password))
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
            Toast.makeText(this, "Akun berhasil dibuat, silahkan login", Toast.LENGTH_LONG).show()
            finish()
        } else {
            showSnackBar()
            Toast.makeText(this, "ada error nih ${result.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun showSnackBar() {
        val snackBar = Snackbar.make(
            binding.root, "Replace with your own action",
            Snackbar.LENGTH_LONG
        ).setAction("Action", null)
        snackBar.setActionTextColor(Color.BLUE)

        val snackBarView = snackBar.view
        snackBarView.setBackgroundColor(Color.CYAN)
        val textView =
            snackBarView.findViewById(com.google.android.material.R.id.snackbar_text) as TextView
        textView.setTextColor(Color.BLUE)
        snackBar.show()
    }


    private fun playAnimation() = with(binding) {
        ObjectAnimator.ofFloat(ivLogo, View.TRANSLATION_X, -25f, 25f).apply {
            duration = 5000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val title = ObjectAnimator.ofFloat(tvTitle, View.ALPHA, 1f).setDuration(500)
        /*val tvName =
            ObjectAnimator.ofFloat(tvName, View.ALPHA, 1f).setDuration(500)
        val fieldName =
            ObjectAnimator.ofFloat(fieldName, View.ALPHA, 1f).setDuration(500)
        val tvEmail =
            ObjectAnimator.ofFloat(tvEmail, View.ALPHA, 1f).setDuration(500)
        val fieldEmail =
            ObjectAnimator.ofFloat(fieldEmail, View.ALPHA, 1f).setDuration(500)
        val tvPassword =
            ObjectAnimator.ofFloat(tvPassword, View.ALPHA, 1f).setDuration(500)*/
        val fieldPassword =
            ObjectAnimator.ofFloat(edRegisterPassword, View.ALPHA, 1f).setDuration(500)
        val signup = ObjectAnimator.ofFloat(btnSignup, View.ALPHA, 1f).setDuration(500)

        AnimatorSet().apply {
            playSequentially(
                title,
                //tvName,
                //fieldName,
                //tvEmail,
                //fieldEmail,
                //tvPassword,
                fieldPassword,
                signup
            )
            startDelay = 300
        }.start()
    }


}