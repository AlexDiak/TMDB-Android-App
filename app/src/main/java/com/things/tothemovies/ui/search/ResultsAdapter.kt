package com.things.tothemovies.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.things.tothemovies.data.local.model.Show
import com.things.tothemovies.databinding.RecyclerViewResultItemBinding
import com.things.tothemovies.utils.getPosterPath
import com.things.tothemovies.utils.setImage

class ResultsAdapter: PagingDataAdapter<Show, ResultsAdapter.ResultsViewHolder>(ITEM_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultsViewHolder {
        val binding = RecyclerViewResultItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return ResultsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ResultsViewHolder, position: Int) {
        val item = getItem(position)
        item?.let { holder.bind(it) }
    }

    inner class ResultsViewHolder(
        private val binding: RecyclerViewResultItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Show) {
            binding.name.text = item.title
            binding.year.text = item.year
            binding.photo.setImage(getPosterPath(item.posterPath))
            binding.root.setOnClickListener {
                onClick?.invoke(item.mediaType, item.id)
            }
        }
    }

    var onClick: ((String, Int) -> Unit)? = null
}

private val ITEM_COMPARATOR = object : DiffUtil.ItemCallback<Show>() {
    override fun areItemsTheSame(oldItem: Show, newItem: Show): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Show, newItem: Show): Boolean {
        return oldItem == newItem
    }
}