package com.phongpn.water.ui.dialog

import android.os.Bundle
import android.view.View
import com.phongpn.water.R
import com.phongpn.water.ui.dialog.adapter.SelectMultiItemAdapter
import com.phongpn.water.ui.dialog.base.BaseBottomSheetDialogFragment
import com.phongpn.water.ui.dialog.base.BaseBodyDialog
import kotlinx.android.synthetic.main.selected_bottom_sheet.*

class SelectedMultiItemBottomSheetFragment(
    private val listContent : List<String>,
    private val listDetail : List<String?>,
    var setItemPosition : MutableSet<Int>,
    parent: BaseBottomSheetDialogFragment,
) : BaseBodyDialog<Set<Int>>(R.layout.selected_bottom_sheet,parent) {
    override fun confirm() {
        confirm?.invoke(setItemPosition)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        list_item.adapter = SelectMultiItemAdapter(
            context!!,
            listContent,
            listDetail,
            setItemPosition
        )
    }
}