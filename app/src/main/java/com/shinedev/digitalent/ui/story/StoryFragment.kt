package com.shinedev.digitalent.ui.story

import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.shinedev.digitalent.R
import com.shinedev.digitalent.ViewModelFactory
import com.shinedev.digitalent.common.animateVisibility
import com.shinedev.digitalent.data.local.pref.AuthPreferenceDataStore
import com.shinedev.digitalent.databinding.FragmentStoryBinding
import com.shinedev.digitalent.databinding.ViewEmptyDataBinding
import com.shinedev.digitalent.ui.login.LoginActivity
import com.shinedev.digitalent.ui.main.MainActivity.Companion.TOKEN
import com.shinedev.digitalent.ui.main.adapter.LoadingStateAdapter
import com.shinedev.digitalent.ui.main.adapter.StoryAdapter
import com.shinedev.digitalent.ui.upload.UploadStoryActivity
import com.shinedev.digitalent.widget.CollectionWidgetProvider
import com.shinedev.digitalent.widget.UpdateWidgetService
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = AuthPreferenceDataStore.AUTH_PREFERENCE)
class StoryFragment : Fragment() {

    private var _binding: FragmentStoryBinding? = null
    private val binding get() = _binding!!

    private lateinit var emptyBinding: ViewEmptyDataBinding

    private lateinit var storyAdapter: StoryAdapter
    private lateinit var viewModel: StoryViewModel
    private lateinit var pref: AuthPreferenceDataStore

    private var token = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStoryBinding.inflate(LayoutInflater.from(requireActivity()))
        emptyBinding = ViewEmptyDataBinding.bind(binding.root)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pref = AuthPreferenceDataStore.getInstance(requireContext().dataStore)
        setupViewModel()
        setupRecyclerView()
        setupAction()
        observeData()
        startJob()
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(
            requireActivity(), ViewModelFactory(requireContext())
        )[StoryViewModel::class.java]
    }

    private fun setupRecyclerView() = with(binding) {
        storyAdapter = StoryAdapter(requireActivity())
        storyAdapter.addLoadStateListener { loadState ->
            if (loadState.source.refresh is LoadState.Loading) {
                emptyBinding.viewEmpty.animateVisibility(false)
                rvStory.animateVisibility(false)
                progressBar.animateVisibility(true)
            } else if ((loadState.source.refresh is LoadState.NotLoading && loadState.append.endOfPaginationReached && storyAdapter.itemCount < 1) || loadState.source.refresh is LoadState.Error) {
                rvStory.animateVisibility(false)
                progressBar.animateVisibility(false)
                emptyBinding.viewEmpty.animateVisibility(true)
            } else {
                progressBar.animateVisibility(false)
                emptyBinding.viewEmpty.animateVisibility(false)
                rvStory.animateVisibility(true)
            }
        }
        rvStory.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = storyAdapter.withLoadStateFooter(footer = LoadingStateAdapter {
                storyAdapter.retry()
            })
        }
    }

    private fun setupAction() = with(binding) {
        btnAddStory.setOnClickListener {
            startActivity(Intent(requireContext(), UploadStoryActivity::class.java))
        }
        appToolbar.apply {
            setNavigationOnClickListener { findNavController().popBackStack() }
            setOnMenuItemClickListener { menuItem ->
                when (menuItem.itemId) {
                    R.id.action_localization -> {
                        startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                    }
                    R.id.action_map -> {
                        findNavController().navigate(R.id.navigation_map)
                    }
                    R.id.action_logout -> {
                        viewModel.logout()
                        val intent = Intent(requireContext(), LoginActivity::class.java).apply {
                            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        }
                        startActivity(intent)
                        requireActivity().finish()
                    }
                }
                true
            }
        }
        swipeRefresh.apply {
            setOnRefreshListener {
                isRefreshing = false
                with(storyAdapter) {
                    refresh()
                    rvStory.smoothScrollToPosition(0)
                }
            }
        }
    }

    private fun observeData() = with(binding.rvStory) {
        lifecycleScope.launch {
            token = activity?.intent?.getStringExtra(TOKEN) ?: pref.getToken().first()
            viewModel.getAllStories(token)
                .observe(viewLifecycleOwner) { pagedListStory ->
                    val state = layoutManager?.onSaveInstanceState()
                    storyAdapter.submitData(lifecycle, pagedListStory)
                    layoutManager?.onRestoreInstanceState(state)

                    sendUpdateStoryList(requireContext())
                }
        }
    }

    private fun startJob() {
        val mServiceComponent = ComponentName(requireActivity(), UpdateWidgetService::class.java)
        val builder = JobInfo.Builder(JOB_ID, mServiceComponent)
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_NONE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            builder.setPeriodic(600000)
        } else {
            builder.setPeriodic(600000)
        }
        val jobScheduler =
            requireActivity().getSystemService(AppCompatActivity.JOB_SCHEDULER_SERVICE) as JobScheduler
        jobScheduler.schedule(builder.build())
    }

    private fun sendUpdateStoryList(context: Context) {
        val i = Intent(context, CollectionWidgetProvider::class.java)
        i.action = CollectionWidgetProvider.UPDATE_WIDGET
        context.sendBroadcast(i)
    }


    override fun onResume() = with(binding) {
        super.onResume()
        with(storyAdapter) {
            refresh()
            rvStory.smoothScrollToPosition(0)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val JOB_ID = 2001
    }

}