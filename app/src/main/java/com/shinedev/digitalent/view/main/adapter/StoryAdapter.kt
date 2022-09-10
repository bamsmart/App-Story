package com.shinedev.digitalent.view.main.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.shinedev.digitalent.R
import com.shinedev.digitalent.common.OnItemClickListener
import com.shinedev.digitalent.databinding.ItemStoryBinding
import com.shinedev.digitalent.view.main.StoryResponse
import com.shinedev.digitalent.view.withDateFormat

class StoryAdapter :
    RecyclerView.Adapter<StoryAdapter.StoryViewHolder>() {

    lateinit var onItemClickListener: OnItemClickListener

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
                    onItemClickListener.onItemClick(data[position], position)
                }
            }
        }
    }

    override fun getItemCount(): Int = data.size

    class StoryViewHolder(val binding: ItemStoryBinding) : RecyclerView.ViewHolder(binding.root)
}
