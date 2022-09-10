package com.shinedev.digitalent.view.splash

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import com.shinedev.digitalent.R
import com.shinedev.digitalent.pref.AuthPreference
import com.shinedev.digitalent.view.login.LoginActivity
import com.shinedev.digitalent.view.main.MainActivity
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = AuthPreference.AUTH_PREFERENCE)

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        setupView()
        val pref = AuthPreference.getInstance(dataStore)
        Handler().postDelayed({
            initDirection(pref)
        }, 1000)
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

    private fun initDirection(pref: AuthPreference) {
        lifecycleScope.launch {
            pref.getIsLogin().catch { e ->
                Log.d("splash", "apakah error")
                e.printStackTrace()
            }.collect {
                Log.d("splash", "apakah isLogin? $it")
                if (it) {
                    startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                } else {
                    startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
                }
                finish()
            }
        }
    }
}