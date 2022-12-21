package com.phongpn.water.ui.dialog.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.phongpn.water.R
import kotlinx.android.synthetic.main.selected_bottom_sheet_item.view.*
import java.lang.Exception

class SelectItemAdapter(
    val context : Context,
    private val listContent : List<String>,
    private val listDetail : List<String?>,
) : BaseAdapter() {
    var selectedPosition = 0
    var onItemSelected : ((Int) -> Unit)? = null
    override fun getCount() = listContent.size

    override fun getItem(position: Int): Any {
        return listContent[position]
    }

    override fun getItemId(position: Int): Long = 0

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = LayoutInflater.from(context).inflate(R.layout.selected_bottom_sheet_item, null, true)
        view.apply {
            setOnClickListener {
                selectedPosition = position
                onItemSelected?.invoke(position) }
            content_item_tv.text = listContent[position]
            try {
                listDetail[position].let {
                    if (it == null ) detail_item_tv.visibility = View.GONE
                    else detail_item_tv.text = it
                }
            }
            catch (e : Exception) {}

            selected_item_iv.visibility = if (selectedPosition == position)  View.VISIBLE else View.GONE
        }
        return view
    }
}