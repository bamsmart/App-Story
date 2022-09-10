package com.shinedev.digitalent.view.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.shinedev.digitalent.R
import com.shinedev.digitalent.ViewModelWithPrefFactory
import com.shinedev.digitalent.databinding.ActivityMainBinding
import com.shinedev.digitalent.pref.AuthPreference
import com.shinedev.digitalent.view.login.LoginActivity
import com.shinedev.digitalent.view.main.adapter.StoryAdapter
import com.shinedev.digitalent.view.story.UploadStoryActivity

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = AuthPreference.AUTH_PREFERENCE)

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var storyAdapter: StoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupView()
        setupAction()
        observeData()
    }

    private fun setupViewModel() {
        val pref = AuthPreference.getInstance(dataStore)
        viewModel =
            ViewModelProvider(
                this,
                ViewModelWithPrefFactory(pref)
            )[MainViewModel::class.java]
    }

    private fun setupView() = with(binding) {
        storyAdapter = StoryAdapter()
        rvStory.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = storyAdapter
        }
    }

    private fun setupAction() = with(binding) {
        viewModel.getListStory()

        btnAddStory.setOnClickListener {
            startActivity(Intent(this@MainActivity, UploadStoryActivity::class.java))
        }
        swipeRefresh.setOnRefreshListener {
            viewModel.getListStory()
            swipeRefresh.isRefreshing = false
        }
        appToolbar.apply {
            setNavigationOnClickListener { onBackPressed() }
            setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.localization -> {
                        startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                    }
                    R.id.logout -> {
                        viewModel.logout()
                        val intent = Intent(this@MainActivity, LoginActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        startActivity(intent)
                        finish()
                    }
                }
                true
            }
        }
    }

    private fun observeData() {
        viewModel.listStory.observe(this@MainActivity) {
            storyAdapter.updateListData(it)
        }
    }
}