package com.stratpoint.basedesignpatternguide.presentation.base

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

abstract class BaseRecyclerViewAdapter<VH : RecyclerView.ViewHolder, T> : RecyclerView.Adapter<VH>() {

    val mainList: MutableList<T> = mutableListOf()

    fun submitList(newList: List<T>) {
        DiffUtil.calculateDiff(BaseDiffUtilCallback(newList, mainList)).dispatchUpdatesTo(this)
        mainList.apply {
            clear()
            addAll(newList)
        }
    }

    abstract fun areItemsTheSame(oldItem: T, newItem: T): Boolean
    abstract fun areContentsTheSame(oldItem: T, newItem: T): Boolean

    inner class BaseDiffUtilCallback(private val newList: List<T>, private val oldList: List<T>) : DiffUtil.Callback() {

        override fun getOldListSize() = oldList.size
        override fun getNewListSize() = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) = this@BaseRecyclerViewAdapter.areItemsTheSame(oldList[oldItemPosition], newList[newItemPosition])

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) = this@BaseRecyclerViewAdapter.areContentsTheSame(oldList[oldItemPosition], newList[newItemPosition])
    }
}
