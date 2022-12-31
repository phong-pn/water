package com.phongpn.water.adapter

import android.text.format.DateUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.phongpn.water.R
import com.phongpn.water.entity.LogDrink
import com.phongpn.water.util.*
import com.phongpn.water.util.constant.params.ML
import com.phongpn.water.util.profileparams.UnitParams
import kotlinx.android.synthetic.main.header_layout.view.*
import kotlinx.android.synthetic.main.log_drink_rv_item.view.*
import java.util.*

class DayLogDrinkAdapter (
    var listLogDrinkHeader : List<LogDrinkHeader>,
    val onLogSelect : (LogDrink) -> Unit
): RecyclerView.Adapter<DayLogDrinkAdapter.LogDrinkViewHolder>() {
    private val TYPE_ICON = 1
    private val TYPE_HEADER_DAY = 0

    inner class LogDrinkViewHolder(itemView: View, val onLogSelect : (LogDrink) -> Unit ) : RecyclerView.ViewHolder(itemView){
        fun bindData(logHeader : LogDrinkHeader){
            val log = logHeader.log
            itemView.apply {
                if (!logHeader.isHeader) header.visibility = View.GONE
                    else header_day.text =  if (DateUtils.isToday(log.cal.timeInMillis))  context.getString(R.string.today) else formatDate(log.cal)
                log_drink_item.setOnClickListener { onLogSelect(log) }
                log_amount.text = log.amount.toString() + "ml"
                log.type.apply {
                    log_drink_type.text = this
                    var iconDrawable = 0
                    when(this){
                        Drink.WATER -> {
                            iconDrawable = R.drawable.icon_water_log
                            log_drink_type.text = context.getText(R.string.water)
                        }
                        Drink.COFFEE -> {
                            log_drink_type.text = context.getText(R.string.coffee)
                            iconDrawable = R.drawable.icon_coffee_log
                        }
                        Drink.TEA -> {
                            log_drink_type.text = context.getText(R.string.tea)
                            iconDrawable = R.drawable.icon_tea_log
                        }
                        Drink.JUICE -> {
                            log_drink_type.text = context.getText(R.string.juice)
                            iconDrawable = R.drawable.icon_juice_log
                        }
                        Drink.SOFT_DRINK -> {
                            log_drink_type.text = context.getText(R.string.soft_drink)
                            iconDrawable = R.drawable.icon_soft_drink_log
                        }
                        Drink.PLANT_MILK -> {
                            log_drink_type.text = context.getText(R.string.plant_milk)
                            iconDrawable = R.drawable.icon_plant_milk_log
                        }
                        Drink.MILK -> {
                            log_drink_type.text = context.getText(R.string.milk)
                            iconDrawable = R.drawable.icon_milk_log
                        }
                        Drink.BEER -> {
                            log_drink_type.text = context.getText(R.string.beer)
                            iconDrawable = R.drawable.icon_beer_log
                        }
                        Drink.WINE -> {
                            log_drink_type.text = context.getText(R.string.wine)
                            iconDrawable = R.drawable.icon_wine_log
                        }
                        else -> {
                            log_drink_type.text = context.getText(R.string.spirits)
                            iconDrawable = R.drawable.icon_spitrits_log
                        }
                    }
                    icon_type.setImageResource(iconDrawable)
                }
                log_time.text = formatTime(log.cal)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (listLogDrinkHeader[position].isHeader) TYPE_HEADER_DAY
        else TYPE_ICON
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : LogDrinkViewHolder{
        val inflater = LayoutInflater.from(parent.context)
        return LogDrinkViewHolder(inflater.inflate(R.layout.log_drink_rv_item, parent, false), onLogSelect)
    }
    override fun onBindViewHolder(holder: LogDrinkViewHolder, position: Int) = holder.bindData(listLogDrinkHeader[position])

    override fun getItemCount() = listLogDrinkHeader.size

    fun update(new : List<List<LogDrink>>) {
        val newListLogDrinkHeader = toLogDrinkHeader(new)
        val diffCallBack = object : DiffCallBack<LogDrinkHeader>(listLogDrinkHeader, newListLogDrinkHeader){
            override fun areItemsTheSame(oldPos: Int, newPos: Int): Boolean {
                  return  try {
                      listLogDrinkHeader[oldPos].log.id == newListLogDrinkHeader[newPos].log.id
                  }catch (e : Exception){false}

            }
        }
        listLogDrinkHeader = newListLogDrinkHeader
        DiffUtil.calculateDiff(diffCallBack).dispatchUpdatesTo(this@DayLogDrinkAdapter)
    }
    private fun toLogDrinkHeader(listLog: List<List<LogDrink>>): List<LogDrinkHeader> {
        val listLogDrinkHeader = mutableListOf<LogDrinkHeader>()
        for (list in listLog){
            for (index in list.indices)
                if (index == 0) listLogDrinkHeader.add(LogDrinkHeader(list[index], true))
                    else listLogDrinkHeader.add(LogDrinkHeader(list[index], false))
        }
        return listLogDrinkHeader
    }
    class LogDrinkHeader(var log: LogDrink, var isHeader : Boolean)
}