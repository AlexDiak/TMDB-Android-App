package com.things.tothemovies.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.things.tothemovies.data.remote.model.Result
import com.things.tothemovies.databinding.RecyclerViewResultItemBinding
import com.things.tothemovies.util.setImage

class ResultsAdapter: ListAdapter<Result, ResultsAdapter.ResultsViewHolder>(ITEM_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultsViewHolder {
        val binding = RecyclerViewResultItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)

        return ResultsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ResultsViewHolder, position: Int) {
        val item: Result = getItem(position)
        holder.bind(item)
    }

    inner class ResultsViewHolder(
        private val binding: RecyclerViewResultItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Result) {
            binding.name.text = item.title
            binding.photo.setImage(item.poster_path)
            binding.root.setOnClickListener {
                onClick?.invoke(item.media_type, item.id)
            }
        }
    }

    var onClick: ((String, Int) -> Unit)? = null
}

private val ITEM_COMPARATOR = object : DiffUtil.ItemCallback<Result>() {
    override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
        return oldItem == newItem
    }
}