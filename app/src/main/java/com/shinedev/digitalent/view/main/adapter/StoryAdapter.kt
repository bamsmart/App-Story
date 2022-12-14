package com.shinedev.digitalent.view.main.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shinedev.digitalent.R
import com.shinedev.digitalent.Transition.DESCRIPTION
import com.shinedev.digitalent.Transition.NAME
import com.shinedev.digitalent.Transition.PROFILE
import com.shinedev.digitalent.databinding.ItemStoryBinding
import com.shinedev.digitalent.view.detail.DetailStoryActivity
import com.shinedev.digitalent.view.detail.DetailStoryActivity.Companion.EXT_STORY_DATA
import com.shinedev.digitalent.data.story.StoryResponse
import com.shinedev.digitalent.common.withDateFormat

class StoryAdapter(private val activity: Activity) :
    RecyclerView.Adapter<StoryAdapter.StoryViewHolder>() {

    private var data: List<StoryResponse> = listOf()

    @SuppressLint("NotifyDataSetChanged")
    fun updateListData(updatedListData: List<StoryResponse>) {
        this.data = updatedListData
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val binding = ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        with(holder.binding) {
            with(data[position]) {
                val context = holder.itemView.context
                Glide.with(context)
                    .load(photoUrl)
                    .into(ivItemPhoto)
                tvItemName.text = name
                tvItemDescription.text = description
                tvItemLastUpdate.text = context.getString(
                    R.string.uploaded_at,
                    createdAt.withDateFormat()
                )

                holder.itemView.setOnClickListener {
                    val intent = Intent(context, DetailStoryActivity::class.java)
                    intent.putExtra(EXT_STORY_DATA, data[position])

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
    }

    override fun getItemCount(): Int = data.size

    class StoryViewHolder(val binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root)
}
