package com.shinedev.digitalent.ui.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.shinedev.digitalent.R
import com.shinedev.digitalent.common.DateFormatter
import com.shinedev.digitalent.data.modules.story.model.StoryResponse
import com.shinedev.digitalent.databinding.ActivityDetailStoryBinding
import java.util.*

class DetailStoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailStoryBinding

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
        storyData = intent?.getParcelableExtra(EXT_STORY_DATA)
        setData(storyData)
    }

    private fun setData(story: StoryResponse?) = with(binding) {
        story?.let {
            Glide.with(this@DetailStoryActivity).load(it.photoUrl).into(ivPreview)
            tvName.text = it.name
            tvCreatedAt.text = this@DetailStoryActivity.getString(
                R.string.uploaded_at,
                DateFormatter.formatDate(it.createdAt, TimeZone.getDefault().id)
            )
            tvDescription.text = it.description
        }
    }

    companion object {
        const val EXT_STORY_DATA = "ext_story_data"
    }
}