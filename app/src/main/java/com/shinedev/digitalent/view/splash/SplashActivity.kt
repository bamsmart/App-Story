package com.shinedev.digitalent.view.splash

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import com.shinedev.digitalent.data.pref.AuthPreference
import com.shinedev.digitalent.databinding.ActivitySplashBinding
import com.shinedev.digitalent.view.login.LoginActivity
import com.shinedev.digitalent.view.main.MainActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = AuthPreference.AUTH_PREFERENCE)

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
        val pref = AuthPreference.getInstance(dataStore)

        playAnimation()
        lifecycleScope.launch {
            delay(3000)
            initDirection(pref)
        }
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

    private fun playAnimation() = with(binding) {
        ObjectAnimator.ofFloat(ivLogo, View.ROTATION, -45f, 45f).apply {
            duration = 3000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
    }

    private suspend fun initDirection(pref: AuthPreference) {
        pref.getIsLogin().catch { e ->
            e.printStackTrace()
        }.collect {
            if (it) {
                startActivity(
                    Intent(this@SplashActivity, MainActivity::class.java)
                )
            } else {
                startActivity(
                    Intent(this@SplashActivity, LoginActivity::class.java)
                )
            }
            finish()
        }
    }
}