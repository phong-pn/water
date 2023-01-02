package com.phongpn.water.adapter

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.phongpn.water.R
import com.phongpn.water.util.Drink
import com.phongpn.water.util.changeGradient
import kotlinx.android.synthetic.main.drink_icon_viewholder_layout.view.*

open class ListDrinkIconAdapter(
    listData: List<DrinkIconData>,
    val onCLickItemView: ((Int) -> Unit)?,
) : BaseAdapter<ListDrinkIconAdapter.DrinkIconData>(listData, null) {
    open inner class DrinkIconViewHolder(
        itemView: View
    ) : BaseViewHolder(itemView, null) {
        lateinit var mData: DrinkIconData
        open fun bindData(data: DrinkIconData, pos: Int) {
            mData = data
            itemView.apply {
                setIcon()
                setColor()
                detail_drink_icon_tv.text = mData.detail
                setOnClickListener {
                    onCLickItemView?.invoke(pos)
                }
            }
        }

        private fun setIcon() {
            itemView.apply {
                val icon = when (mData.type) {
                    Drink.WATER -> R.drawable.water_icon
                    Drink.COFFEE -> R.drawable.coffe_icon
                    Drink.TEA -> R.drawable.tea_icon
                    Drink.JUICE -> R.drawable.juice_icon
                    Drink.SOFT_DRINK -> R.drawable.soda_icon
                    Drink.PLANT_MILK -> R.drawable.plant_milk_icon
                    Drink.MILK -> R.drawable.milk_icon
                    Drink.BEER -> R.drawable.beer_icon
                    Drink.WINE -> R.drawable.wine_icon
                    else -> R.drawable.spirits
                }
                drink_icon_iv.setImageResource(icon)
            }
        }

        private fun setColor() {
            itemView.apply {
                val blue = ContextCompat.getColor(context!!, R.color.blue)
                val red = ContextCompat.getColor(context!!, R.color.red_error)
                val background =
                    ContextCompat.getDrawable(context!!, R.drawable.icon_drink_container)
                var iconTint = 0
                if (!mData.isSelected) {
                    var startColor: Int
                    var endColor = 0
                    when (mData.type) {
                        Drink.WINE, Drink.BEER, Drink.SPIRITS -> {
                            startColor = Color.parseColor("#FAA0A0")
                            endColor = Color.parseColor("#FFFFFF")
                            iconTint = red
                        }
                        else -> {
                            startColor = Color.parseColor("#A0C9FA")
                            endColor = Color.parseColor("#FFFFFF")
                            iconTint = blue
                        }
                    }
                    background!!.changeGradient(
                        startColor,
                        endColor,
                        GradientDrawable.Orientation.BOTTOM_TOP
                    )
                } else {
                    iconTint = Color.WHITE
                    var color = when (mData.type) {
                        Drink.WINE, Drink.BEER, Drink.SPIRITS -> red
                        else -> blue
                    }
                    background!!.setTint(color)
                }
                drink_icon_iv.apply {
                    this.background = background
                    setColorFilter(iconTint)
                }

            }
        }

        override fun bindData(data: DrinkIconData) {
            TODO("Not yet implemented")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = DrinkIconViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.drink_icon_viewholder_layout, parent, false)
    )

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        (holder as DrinkIconViewHolder).bindData(mListData[position], position)
    }

    override fun initDiffCallback(new: List<DrinkIconData>) {
        diffCallBack = object : DiffCallBack<DrinkIconData>(mListData, new) {
            override fun areItemsTheSame(oldPos: Int, newPos: Int): Boolean {
                return old[oldPos].type == new[newPos].type
            }

        }
    }

    class DrinkIconData(
        var type: String,
        var detail: String,
        var isSelected: Boolean,
        var amount: Int = 0
    ) {
        constructor(type: String, amount: Int, isSelected: Boolean) : this(
            type,
            "$amount ml",
            isSelected, amount
        )
    }

}