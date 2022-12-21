package com.phongpn.water.ui.dialog

import android.os.Bundle
import android.view.View
import com.phongpn.water.R
import com.phongpn.water.ui.dialog.adapter.SelectItemAdapter
import com.phongpn.water.ui.dialog.base.BaseBottomSheetDialogFragment
import com.phongpn.water.ui.dialog.base.BaseBodyDialog
import kotlinx.android.synthetic.main.selected_bottom_sheet.*

class SelectedBottomSheetFragment(
    private val listContent : List<String>,
    private val listDetail : List<String?>,
    var itemSelected : Int,
    parent: BaseBottomSheetDialogFragment
) : BaseBodyDialog<Int>(R.layout.selected_bottom_sheet, parent){

    private var onItemChange : ((Int) -> Unit)? = null
    fun onItemChanged(listener : (Int) -> Unit) = this.apply { onItemChange = listener }
    override fun confirm() {
        confirm?.invoke(itemSelected)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        list_item.adapter = SelectItemAdapter(
            context!!,
            listContent,
            listDetail
        ).apply {
            selectedPosition = itemSelected
            onItemSelected = {
                notifyDataSetChanged()
                itemSelected = it
                onItemChange?.invoke(it)
            }
        }
    }
}