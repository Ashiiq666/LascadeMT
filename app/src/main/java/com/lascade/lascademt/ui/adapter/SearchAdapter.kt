package com.lascade.lascademt.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.lascade.lascademt.data.model.GalaxyItem
import com.lascade.lascademt.databinding.ItemGalaxiesBinding

class SearchAdapter : ListAdapter<GalaxyItem, SearchAdapter.SearchViewHolder>(DiffCallBack()) {
    inner class SearchViewHolder(private val binding: ItemGalaxiesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: GalaxyItem) {
            binding.apply {
                tvTitle.text = item.title
                tvDescription.text = item.description
                ivThumbnail.load(item.imageUrl)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchViewHolder {
        return SearchViewHolder(
            ItemGalaxiesBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SearchViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallBack : DiffUtil.ItemCallback<GalaxyItem>() {
        override fun areItemsTheSame(oldItem: GalaxyItem, newItem: GalaxyItem): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(
            oldItem: GalaxyItem,
            newItem: GalaxyItem
        ): Boolean {
            return oldItem == newItem
        }
    }
}
