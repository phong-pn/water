package com.phongpn.water.adapter

import android.view.View
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<T>(
    var mListData : List<T>,
    var mOnCLickItemView : ((T) -> Unit)?
) : RecyclerView.Adapter<BaseAdapter<T>.BaseViewHolder>() {
    lateinit var diffCallBack : DiffCallBack<T>
    abstract inner class BaseViewHolder(itemView: View, onCLickItemView: ((T) -> Unit)?) :
        RecyclerView.ViewHolder(itemView) {
        abstract fun bindData(data: T)
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) =
        holder.bindData(mListData[position])

    override fun getItemCount() = mListData.size

    open fun update(new : List<T>){
        initDiffCallback(new)
        mListData = new
        DiffUtil.calculateDiff(diffCallBack).dispatchUpdatesTo(this)
    }

    abstract fun initDiffCallback(new : List<T>)
}