package com.shinedev.digitalent.view.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.shinedev.digitalent.R
import com.shinedev.digitalent.ViewModelWithPrefFactory
import com.shinedev.digitalent.common.OnItemClickListener
import com.shinedev.digitalent.databinding.ActivityMainBinding
import com.shinedev.digitalent.pref.AuthPreference
import com.shinedev.digitalent.view.detail.DetailStoryActivity
import com.shinedev.digitalent.view.detail.DetailStoryActivity.Companion.EXT_STORY_DATA
import com.shinedev.digitalent.view.login.LoginActivity
import com.shinedev.digitalent.view.main.adapter.StoryAdapter
import com.shinedev.digitalent.view.upload.UploadStoryActivity

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
        storyAdapter.onItemClickListener = object : OnItemClickListener {
            override fun onItemClick(item: Any, position: Int) {
                val data = item as StoryResponse

                val intent = Intent(this@MainActivity, DetailStoryActivity::class.java).apply {
                    putExtra(EXT_STORY_DATA, data)
                }
                startActivity(intent)
            }
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
                    R.id.action_localization -> {
                        startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                    }
                    R.id.action_logout -> {
                        viewModel.logout()
                        val intent =
                            Intent(this@MainActivity, LoginActivity::class.java).apply {
                                flags =
                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
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

    override fun onResume() {
        super.onResume()
        Log.d("MainActivity","onResume")
        viewModel.getListStory()
    }
}