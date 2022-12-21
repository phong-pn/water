package com.phongpn.water.ui.dialog.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import com.phongpn.water.R
import kotlinx.android.synthetic.main.selected_bottom_sheet_item.view.*
import java.lang.Exception

class SelectMultiItemAdapter(
    val context : Context,
    private val listContent : List<String>,
    private val listDetail : List<String?>,
    private var listSelectedPosition : MutableSet<Int>
) : BaseAdapter() {
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
                selected_item_iv.visibility = if (listSelectedPosition.add(position)) View.VISIBLE else {
                    listSelectedPosition.remove(position)
                    View.GONE
                }
                onItemSelected?.invoke(position)
            }
            content_item_tv.text = listContent[position]
            listSelectedPosition.forEach { if (it == position) selected_item_iv.visibility = View.VISIBLE }
            try {
                listDetail[position].let {
                    if (it == null ) detail_item_tv.visibility = View.GONE
                    else detail_item_tv.text = it
                }
            }
            catch (e : Exception) {

            }
        }
        return view
    }
}