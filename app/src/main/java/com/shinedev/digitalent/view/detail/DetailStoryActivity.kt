package com.shinedev.digitalent.view.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.shinedev.digitalent.R
import com.shinedev.digitalent.ViewModelFactory
import com.shinedev.digitalent.databinding.ActivityDetailStoryBinding
import com.shinedev.digitalent.view.main.StoryResponse
import com.shinedev.digitalent.view.withDateFormat

class DetailStoryActivity : AppCompatActivity() {
    lateinit var binding: ActivityDetailStoryBinding
    lateinit var viewModel: DetailStoryViewModel

    private var storyData: StoryResponse? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.appToolbar.apply {
            setNavigationOnClickListener {
                finish()
            }
        }
        viewModel =
            ViewModelProvider(this, ViewModelFactory())[DetailStoryViewModel::class.java]

        storyData = intent?.getParcelableExtra(EXT_STORY_DATA)
        setData(storyData)
    }

    private fun setData(story: StoryResponse?) = with(binding) {
        story?.let {
            Glide.with(this@DetailStoryActivity).load(it.photoUrl).into(ivPreview)
            tvName.text = it.name
            tvCreatedAt.text = this@DetailStoryActivity.getString(
                R.string.uploaded_at,
                it.createdAt.withDateFormat()
            )
            tvDescription.text = it.description
        }
    }

    companion object {
        const val EXT_STORY_DATA = "ext_story_data"
    }
}