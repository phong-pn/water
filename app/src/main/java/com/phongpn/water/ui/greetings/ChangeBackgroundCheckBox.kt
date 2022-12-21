package com.phongpn.water.ui.greetings

import android.graphics.Color
import android.widget.CheckBox
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.phongpn.water.R

/*
change color for card view hold textview and checkbox, which declared in greeting fragments
 */
fun changeColorCheckBox(card : CardView, text: TextView, checkBox: CheckBox, select : Boolean){
    card.apply {
        val blue = context.getColor(R.color.blue)
        val white = Color.WHITE
        if (select){
            setCardBackgroundColor(blue)
            text.setTextColor(white)
            checkBox.isChecked = true
        }
        else{
            setCardBackgroundColor(white)
            text.setTextColor(blue)
            checkBox.isChecked = false
        }
    }

}