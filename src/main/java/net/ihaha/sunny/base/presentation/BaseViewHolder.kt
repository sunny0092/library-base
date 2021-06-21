package net.ihaha.sunny.base.presentation

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import timber.log.Timber

abstract class BaseViewHolder(private val binding: ViewDataBinding)  : RecyclerView.ViewHolder(binding.root) {
    fun bind(adapterPosition: Int) {
        binding.root.transitionName = "Transition_${binding.root.id}$adapterPosition"
        Timber.tag("Transition_Animation").d(binding.root.transitionName)
    }
}