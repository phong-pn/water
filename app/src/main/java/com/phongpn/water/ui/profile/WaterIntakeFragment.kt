package com.phongpn.water.ui.profile

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.phongpn.water.R
import com.phongpn.water.ui.dialog.base.BaseBottomSheetDialogFragment
import com.phongpn.water.ui.dialog.SelectedBottomSheetFragment
import com.phongpn.water.ui.greetings.changeColorCheckBox
import com.phongpn.water.util.*
import com.phongpn.water.util.constant.params.*
import com.phongpn.water.util.profileparams.UnitParams
import com.phongpn.water.util.profileparams.WaterIntakeParams
import com.phongpn.water.util.profileparams.WaterIntakeParams.Companion.ACTIVE
import com.phongpn.water.util.profileparams.WaterIntakeParams.Companion.COLD
import com.phongpn.water.util.profileparams.WaterIntakeParams.Companion.HOT
import com.phongpn.water.util.profileparams.WaterIntakeParams.Companion.LIGHT_ACTIVITY
import com.phongpn.water.util.profileparams.WaterIntakeParams.Companion.SEDENTARY
import com.phongpn.water.util.profileparams.WaterIntakeParams.Companion.TEMPERATE
import com.phongpn.water.util.profileparams.WaterIntakeParams.Companion.VERY_ACTIVE
import kotlinx.android.synthetic.main.water_intake_fragment.*

class WaterIntakeFragment(title: String) : BaseDetailProfileFragment(title)  {
    constructor() : this("")
    private val typefaceBold = Typeface.DEFAULT_BOLD
    private val listPhysicalActivity = mutableListOf<String>()
    private val listDetailPhysicalActivity = mutableListOf<String?>()
    private val listClimate = mutableListOf<String>()
    private val listDetailClimate = mutableListOf<String?>()
    private val unitParams = UnitParams.getInstance()
    private val waterIntakeParams = WaterIntakeParams.getInstance()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        WaterIntakeParams.apply {
            listClimate.addAll(listOf(
                getString(R.string.cold),
                getString(R.string.temperate),
                getString(R.string.hot)))
            listDetailPhysicalActivity.addAll(listOf(
                null,
                getString(R.string.less_1_hour),
                getString(R.string.more_1_hour)
            ))
            listPhysicalActivity.addAll(listOf(
                getString(R.string.sedentary),
                getString(R.string.light),
                getString(R.string.high),
                getString(R.string.physical_labor)))
        }
        return inflater.inflate(R.layout.water_intake_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (waterIntakeParams.sex == WaterIntakeParams.MALE){
            changeColorCheckBox(male_cv, male_tv, male_cb, true)

        }
        else changeColorCheckBox(female_cv, female_tv, female_cb, true)
        unit_weight_picker.apply {
            typeface = typefaceBold
            setSelectedTypeface(typefaceBold)
            val unit = arrayOf(KG, LBS)
            displayedValues = unit
            minValue = 0
            maxValue = 1
            value = when(unitParams.unitWeight){
                KG -> 0
                else -> 1
            }
            setOnValueChangedListener { _, _, newVal -> unitParams.unitWeight = unit[newVal] }
        }
        weight_picker.apply {
            typeface = typefaceBold
            setSelectedTypeface(typefaceBold)
            minValue = 1
            setupWeightPicker()
            setOnValueChangedListener { _, _, newVal -> waterIntakeParams.weight = newVal }
        }

        amount_drink_picker.apply {
            typeface = typefaceBold
            setSelectedTypeface(typefaceBold)
            minValue = 1
            setupAmountPicker()
        }

        unit_drink_picker.apply {
            typeface = typefaceBold
            setSelectedTypeface(typefaceBold)
            val unit = arrayOf(ML, OZ_UK, OZ_US)
            displayedValues = unit
            minValue = 0
            maxValue = unit.size - 1
            value = when(unitParams.unitDrink){
                ML -> 0
                OZ_UK -> 1
                else -> 2
            }
            setOnValueChangedListener { _, _, newVal -> unitParams.unitDrink = unit[newVal] }
        }
        setupAmountPicker()
        setupWeightPicker()

        addListener()
        addObserver()
    }

    private fun addObserver() {
        unitParams.observe { type, _ ->
            when(type){
                UnitParams.UNIT_WEIGHT ->{
                    setupWeightPicker()
                }
                UnitParams.UNIT_DRINK -> {
                    setupAmountPicker()
                }
            }
        }
        waterIntakeParams.observe { type, _ ->
            when (type) {
                WaterIntakeParams.AMOUNT -> {
                    setupAmountPicker()
                }
            }
        }
    }

    private fun setupWeightPicker() {
        val MAX_KG = 250
        val MAX_LBS = MAX_KG.toLbs(KG)
        weight_picker.apply {
            try {
                if (unitParams.unitWeight == KG){
                    maxValue = MAX_KG
                    value = waterIntakeParams.weight
                }
                else {
                    maxValue = MAX_LBS
                    value = waterIntakeParams.weight.toLbs(KG)
                }
            }
            catch (e :Exception){
                e.printStackTrace()
            }

        }

    }

    private fun setupAmountPicker() {
        val MAX_ML = 5000
        val MAX_OZ_UK = MAX_ML.toOz_Uk(ML)
        val MAX_OZ_US = MAX_ML.toOz_Us(ML)
        amount_drink_picker.apply {
            try{
                when(unitParams.unitDrink){
                    ML ->{
                        var display = arrayOf<String>()
                        maxValue = 1
                        for (index in minValue * 100 until MAX_ML step 100) display = display.plus(index.toString())
                        displayedValues = display
                        minValue = 0
                        maxValue = display.size - 1
                        value = display.indexOf(waterIntakeParams.amount.toMl(ML).toString())
                        setOnValueChangedListener { _, _, newVal -> waterIntakeParams.amount = display[newVal].toInt() }
                    }
                    OZ_UK -> {
                        displayedValues = null
                        minValue = 1
                        maxValue = MAX_OZ_UK
                        value = waterIntakeParams.amount.toOz_Uk(ML)
                        setOnValueChangedListener { _, _, newVal -> waterIntakeParams.amount = newVal.toMl(
                            OZ_UK) }
                    }
                    OZ_US -> {
                        displayedValues = null
                        minValue = 1
                        maxValue = MAX_OZ_US
                        value = waterIntakeParams.amount.toOz_Us(ML)
                        setOnValueChangedListener { _, _, newVal -> waterIntakeParams.amount = newVal.toMl(
                            OZ_US) }
                    }
                }
            }
            catch (e : Exception){
                e.printStackTrace()
            }


        }
    }

    private fun addListener() {
        male_cv.setOnClickListener {
            changeColorCheckBox(male_cv, male_tv, male_cb, true)
            changeColorCheckBox(female_cv, female_tv, female_cb, false)
            waterIntakeParams.sex = WaterIntakeParams.MALE
        }
        female_cv.setOnClickListener {
            changeColorCheckBox(male_cv, male_tv, male_cb, false)
            changeColorCheckBox(female_cv, female_tv, female_cb, true)
            waterIntakeParams.sex = WaterIntakeParams.FEMALE
        }
        waterIntakeParams.apply {
            physical_activity_bt.apply {
                text = when(activity){
                    SEDENTARY -> listPhysicalActivity[0]
                    LIGHT_ACTIVITY -> listPhysicalActivity[1]
                    ACTIVE -> listPhysicalActivity[2]
                    else -> listPhysicalActivity[3]
                }
                setOnClickListener {
                    BaseBottomSheetDialogFragment(getString(R.string.choose_activity), this@WaterIntakeFragment).run {
                        onDialogShow = {
                            setContentView(
                                SelectedBottomSheetFragment(listPhysicalActivity, listDetailPhysicalActivity, when(waterIntakeParams.activity){
                                    SEDENTARY -> 0
                                    LIGHT_ACTIVITY -> 1
                                    ACTIVE -> 2
                                    else -> 3
                                },this).apply {
                                    confirm = {
                                        waterIntakeParams.activity = when(it){
                                            0 -> SEDENTARY
                                            1 -> LIGHT_ACTIVITY
                                            2 -> ACTIVE
                                            else -> VERY_ACTIVE
                                        }
                                        text = listPhysicalActivity[it]

                                    }
                                }
                            )
                        }
                        show()
                    }

                }
            }
            climate_bt.apply {
                text = when(weather){
                    WaterIntakeParams.COLD -> listClimate[0]
                    WaterIntakeParams.TEMPERATE -> listClimate[1]
                    else -> listClimate[2]
                }
                setOnClickListener {
                    BaseBottomSheetDialogFragment(getString(R.string.choose_climate), this@WaterIntakeFragment).run {
                        onDialogShow = {
                            setContentView(
                                SelectedBottomSheetFragment(listClimate, listDetailClimate, when(weather){
                                    COLD -> 0
                                    TEMPERATE -> 1
                                    else  -> 2
                                }, this).apply {
                                    confirm = {
                                        weather = when(it){
                                            0 -> COLD
                                            1 -> TEMPERATE
                                            else -> HOT
                                        }
                                        text = listClimate[it]
                                    }
                                }
                            )
                        }
                        show()
                    }
                    }
                }
            }
        }

    }
