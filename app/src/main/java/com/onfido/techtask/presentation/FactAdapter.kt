package com.onfido.techtask.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.onfido.techtask.databinding.ItemFactBinding
import com.onfido.techtask.domain.Fact

class FactAdapter : ListAdapter<Fact, FactAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemFactBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.binding.apply {
            text.text = item.text
            icVerified.isVisible = item.verified
            icNew.isVisible = item.isNew
        }
    }

    class ViewHolder(val binding: ItemFactBinding) : RecyclerView.ViewHolder(binding.root)

    private object DiffCallback : DiffUtil.ItemCallback<Fact>() {
        override fun areItemsTheSame(oldItem: Fact, newItem: Fact): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: Fact, newItem: Fact): Boolean =
            oldItem == newItem
    }
}