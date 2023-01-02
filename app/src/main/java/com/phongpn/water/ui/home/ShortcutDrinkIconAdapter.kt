package com.phongpn.water.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.AnimationSet
import android.view.animation.TranslateAnimation
import android.widget.ImageView
import com.phongpn.water.R
import com.phongpn.water.adapter.ListDrinkIconAdapter
import kotlinx.android.synthetic.main.drink_icon_main_fragment_viewholder_layout.view.*

class ShortcutDrinkIconAdapter(
    listData: List<DrinkIconData>,
    onCLickItemView: ((Int) -> Unit)?
) : ListDrinkIconAdapter(listData, onCLickItemView) {

    inner class IconViewHolder(itemView: View) : DrinkIconViewHolder(itemView) {
        override fun bindData(data: DrinkIconData, pos: Int) {
            super.bindData(data, pos)
            itemView.apply {
                detail_drink_icon_tv.text = data.detail
                data.detail = "${data.amount} ml"
                detail_drink_icon_tv.text = data.detail
                setOnClickListener {
                    onCLickItemView?.invoke(pos)
                    drink_icon_iv.invalidate()
                    val drawable = drink_icon_iv.drawable
                    val background = drink_icon_iv.background
                    second_icon_iv.apply {
                        setImageDrawable(drawable)
                        scaleType = ImageView.ScaleType.CENTER
                        this.background = background
                        var translateAnimation: TranslateAnimation
                        resources.displayMetrics.apply {
                            var location = intArrayOf(1, 1)
                            getLocationOnScreen(location)
                            translateAnimation = TranslateAnimation(
                                0f,
                                (widthPixels / 2).toFloat() - location[0].toFloat(),
                                0f,
                                (heightPixels / 3).toFloat() - location[1].toFloat()
                            )

                        }
                        val animationSet = AnimationSet(true).apply {
                            addAnimation(AlphaAnimation(1f, 0f))
                            addAnimation(translateAnimation)
                            duration = 1500
                        }
//                        animation = animationSet
//                        animate().setDuration(1500).start()
                        startAnimation(animationSet)
                    }
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DrinkIconViewHolder =
        IconViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.drink_icon_main_fragment_viewholder_layout, parent, false)
        )
}