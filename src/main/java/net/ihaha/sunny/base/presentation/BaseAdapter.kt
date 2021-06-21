package net.ihaha.sunny.base.presentation

import androidx.recyclerview.widget.RecyclerView
import net.ihaha.sunny.base.presentation.listener.BindAdapter


abstract class BaseAdapter<VH : RecyclerView.ViewHolder, T, K> : RecyclerView.Adapter<VH>(), BindAdapter<T, K>