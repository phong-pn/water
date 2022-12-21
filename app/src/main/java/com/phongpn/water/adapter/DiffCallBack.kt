package com.phongpn.water.adapter

import androidx.recyclerview.widget.DiffUtil

open class DiffCallBack<T>(
     val old : List<T>,
    val new : List<T>
) : DiffUtil.Callback() {
    override fun getOldListSize() = old.size

    override fun getNewListSize() = new.size

    override fun areItemsTheSame(oldPos: Int, newPos: Int)  = old[oldPos].hashCode() == new[newPos].hashCode()

    override fun areContentsTheSame(oldPos: Int, newPos: Int) = old[oldPos].hashCode() == new[newPos].hashCode()
}