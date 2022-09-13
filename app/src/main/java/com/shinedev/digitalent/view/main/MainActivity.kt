package com.shinedev.digitalent.view.main

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.shinedev.digitalent.R
import com.shinedev.digitalent.appwidget.CollectionWidgetProvider
import com.shinedev.digitalent.appwidget.UpdateWidgetService
import com.shinedev.digitalent.common.hide
import com.shinedev.digitalent.common.show
import com.shinedev.digitalent.database.StoryDatabase
import com.shinedev.digitalent.database.dao.StoryDao
import com.shinedev.digitalent.database.entity.StoryEntity
import com.shinedev.digitalent.databinding.ActivityMainBinding
import com.shinedev.digitalent.databinding.ViewEmptyDataBinding
import com.shinedev.digitalent.pref.AuthPreference
import com.shinedev.digitalent.view.login.LoginActivity
import com.shinedev.digitalent.view.main.adapter.StoryAdapter
import com.shinedev.digitalent.view.upload.UploadStoryActivity


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = AuthPreference.AUTH_PREFERENCE)

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var emptyBinding: ViewEmptyDataBinding
    private lateinit var storyAdapter: StoryAdapter

    private lateinit var viewModel: MainViewModel
    lateinit var database: StoryDatabase
    lateinit var storyDao: StoryDao
    lateinit var repository: StoryRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        emptyBinding = ViewEmptyDataBinding.bind(binding.root)
        setContentView(binding.root)

        setupViewModel()
        setupView()
        setupAction()
        observeData()
        startJob()
    }

    private fun setupViewModel() {
        val pref = AuthPreference.getInstance(dataStore)
        database = StoryDatabase.getInstance(this)
        storyDao = database.storyDao()
        repository = StoryRepository(storyDao)

        viewModel =
            ViewModelProvider(
                this,
                MainViewModelFactory(pref, repository)
            )[MainViewModel::class.java]
    }

    private fun setupView() = with(binding) {
        val activity = this@MainActivity
        storyAdapter = StoryAdapter(activity)
        rvStory.apply {
            layoutManager = LinearLayoutManager(activity)
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

    private fun observeData() = with(binding) {
        viewModel.apply {
            val owner = this@MainActivity
            listStory.observe(owner) { it ->
                if (it.isNotEmpty()) {
                    emptyBinding.viewEmpty.hide()
                    storyAdapter.updateListData(it)

                    val newData = it.sortedByDescending { sorted -> sorted.createdAt }
                        .filterIndexed { idx, _ -> idx < 5 }.map { story ->
                            StoryEntity(0, story.name, story.photoUrl)
                        }
                    viewModel.insetUpdatedListStory(newData)

                } else {
                    emptyBinding.viewEmpty.show()
                }
            }
            isLoading.observe(owner) {
                it?.let { progressBar.isVisible = it }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.getListStory()
    }

    private fun startJob() {
        val JOB_ID = 2001
        val mServiceComponent = ComponentName(this, UpdateWidgetService::class.java)
        val builder = JobInfo.Builder(JOB_ID, mServiceComponent)
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_NONE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //builder.setPeriodic(900000) //15 menit
            builder.setPeriodic(5000) //10 detik
        } else {
            builder.setPeriodic(3000) //5 detik
        }
        val jobScheduler = getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
        jobScheduler.schedule(builder.build())
        Log.d("WIDGET UPDATE SERVICES", "Service Started!")
    }

    fun sendUpdateStoryList(context: Context) {
        val i = Intent(context, CollectionWidgetProvider::class.java)
        i.action = CollectionWidgetProvider.UPDATE_WIDGET
        context.sendBroadcast(i)
    }
}