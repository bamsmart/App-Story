package com.shinedev.digitalent.ui.main.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shinedev.digitalent.R
import com.shinedev.digitalent.Transition.DESCRIPTION
import com.shinedev.digitalent.Transition.NAME
import com.shinedev.digitalent.Transition.PROFILE
import com.shinedev.digitalent.common.DateFormatter
import com.shinedev.digitalent.data.modules.story.model.StoryResponse
import com.shinedev.digitalent.databinding.ItemStoryBinding
import com.shinedev.digitalent.ui.detail.DetailStoryActivity
import com.shinedev.digitalent.ui.detail.DetailStoryActivity.Companion.EXT_STORY_DATA
import java.util.*

class StoryAdapter(private val activity: Activity) :
    PagingDataAdapter<StoryResponse, StoryAdapter.StoryViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(activity, data)
        }
    }

    class StoryViewHolder(private val binding: ItemStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(activity: Activity, data: StoryResponse) = with(binding) {
            val context = itemView.context
            Glide.with(context)
                .load(data.photoUrl)
                .into(ivItemPhoto)
            tvItemName.text = data.name
            tvItemDescription.text = data.description
            tvItemLastUpdate.text = context.getString(
                R.string.uploaded_at,
                DateFormatter.formatDate(data.createdAt, TimeZone.getDefault().id)
            )

            itemView.setOnClickListener {
                val intent = Intent(context, DetailStoryActivity::class.java)
                intent.putExtra(EXT_STORY_DATA, data)

                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        activity,
                        Pair(ivItemPhoto, PROFILE),
                        Pair(tvItemName, NAME),
                        Pair(tvItemDescription, DESCRIPTION)
                    )
                context.startActivity(intent, optionsCompat.toBundle())
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StoryResponse>() {
            override fun areItemsTheSame(oldItem: StoryResponse, newItem: StoryResponse): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: StoryResponse,
                newItem: StoryResponse
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}
