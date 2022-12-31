package com.phongpn.water.ui.profile

import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import com.phongpn.water.R
import com.phongpn.water.storage.SharePrefUtil
import com.phongpn.water.ui.dialog.SelectedBottomSheetFragment
import com.phongpn.water.ui.dialog.base.BaseBottomSheetDialogFragment
import com.phongpn.water.ui.greetings.changeColorCheckBox
import com.phongpn.water.util.constant.params.*
import com.phongpn.water.util.profileparams.UnitParams
import com.phongpn.water.util.profileparams.WaterIntakeParams
import com.phongpn.water.util.profileparams.WaterIntakeParams.Companion.ACTIVE
import com.phongpn.water.util.profileparams.WaterIntakeParams.Companion.COLD
import com.phongpn.water.util.profileparams.WaterIntakeParams.Companion.LIGHT_ACTIVITY
import com.phongpn.water.util.profileparams.WaterIntakeParams.Companion.SEDENTARY
import com.phongpn.water.util.profileparams.WaterIntakeParams.Companion.TEMPERATE
import com.phongpn.water.util.toLbs
import com.phongpn.water.util.toMl
import com.phongpn.water.util.toOz_Uk
import com.phongpn.water.util.toOz_Us
import com.phongpn.water.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.water_intake_fragment.*

class WaterIntakeFragment(title: String) : BaseDetailProfileFragment(title) {
    constructor() : this("")

    private val typefaceBold = Typeface.DEFAULT_BOLD
    private val listPhysicalActivity = mutableListOf<String>()
    private val listDetailPhysicalActivity = mutableListOf<String?>()
    private val listClimate = mutableListOf<String>()
    private val listDetailClimate = mutableListOf<String?>()
    private val unitParams = UnitParams.getInstance()
    private val waterIntakeParams = WaterIntakeParams.getInstance()
    private val mainViewModel by activityViewModels<MainViewModel>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        listClimate.addAll(
            listOf(
                getString(R.string.cold),
                getString(R.string.temperate),
                getString(R.string.hot)
            )
        )
        listDetailPhysicalActivity.addAll(
            listOf(
                null,
                getString(R.string.less_1_hour),
                getString(R.string.more_1_hour)
            )
        )
        listPhysicalActivity.addAll(
            listOf(
                getString(R.string.sedentary),
                getString(R.string.light),
                getString(R.string.high),
                getString(R.string.physical_labor)
            )
        )
        return inflater.inflate(R.layout.water_intake_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (waterIntakeParams.sex == WaterIntakeParams.MALE) {
            changeColorCheckBox(male_cv, male_tv, male_cb, true)

        } else changeColorCheckBox(female_cv, female_tv, female_cb, true)
        unit_weight_picker.apply {
            typeface = typefaceBold
            setSelectedTypeface(typefaceBold)
            val unit = arrayOf(KG, LBS)
            displayedValues = unit
            minValue = 0
            maxValue = 1
            value = when (unitParams.unitWeight) {
                KG -> 0
                else -> 1
            }
            setOnValueChangedListener { _, _, newVal -> mainViewModel.setUnitWeight(unit[newVal]) }
        }
        weight_picker.apply {
            typeface = typefaceBold
            setSelectedTypeface(typefaceBold)
            minValue = 1
        }

        amount_drink_picker.apply {
            typeface = typefaceBold
            setSelectedTypeface(typefaceBold)
            minValue = 1
        }

        addListener()
        addObserver()
    }

    private fun addObserver() {
        mainViewModel.apply {
            unitWeight.observe(viewLifecycleOwner) {
                setupWeightPicker(it)
            }

            unitDrink.observe(viewLifecycleOwner) {
                setUpGoalPicker()
            }

            goal.observe(viewLifecycleOwner) {
                setUpGoalPicker()
            }

            activity.observe(viewLifecycleOwner) {
                physical_activity_bt.text = when (it) {
                    SEDENTARY -> getString(R.string.sedentary)
                    LIGHT_ACTIVITY -> getString(R.string.light)
                    ACTIVE -> getString(R.string.high)
                    else -> getString(R.string.physical_labor)
                }
            }

            weather.observe(viewLifecycleOwner) {
                climate_bt.text = when (it) {
                    COLD -> getString(R.string.cold)
                    TEMPERATE -> getString(R.string.temperate)
                    else -> getString(R.string.hot)
                }
            }
        }
    }

    private fun setupWeightPicker(unitWeight: String) {
        val MAX_KG = 250
        val MAX_LBS = MAX_KG.toLbs(KG)
        weight_picker.apply {
            try {
                if (unitWeight == KG) {
                    maxValue = MAX_KG
                    value = SharePrefUtil.weight
                } else {
                    maxValue = MAX_LBS
                    value = SharePrefUtil.weight.toLbs(KG)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }

    }

    private fun setUpGoalPicker() {
        val MAX_ML = 5000
        val MAX_OZ_UK = MAX_ML.toOz_Uk(ML)
        val MAX_OZ_US = MAX_ML.toOz_Us(ML)
        amount_drink_picker.apply {
            try {
                when (SharePrefUtil.unitDrink) {
                    ML -> {
                        var display = arrayOf<String>()
                        maxValue = 1
                        for (index in minValue * 100 until MAX_ML step 100) display =
                            display.plus(index.toString())
                        displayedValues = display
                        minValue = 0
                        maxValue = display.size - 1
                        value = display.indexOf(SharePrefUtil.goal.toMl(ML).toString())
                        setOnValueChangedListener { _, _, newVal ->
                            waterIntakeParams.amount = display[newVal].toInt()
                        }
                    }
                    OZ_UK -> {
                        displayedValues = null
                        minValue = 1
                        maxValue = MAX_OZ_UK
                        value = SharePrefUtil.goal.toOz_Uk(ML)
                        setOnValueChangedListener { _, _, newVal ->
                            waterIntakeParams.amount = newVal.toMl(
                                OZ_UK
                            )
                        }
                    }
                    OZ_US -> {
                        displayedValues = null
                        minValue = 1
                        maxValue = MAX_OZ_US
                        value = SharePrefUtil.goal.toOz_Us(ML)
                        setOnValueChangedListener { _, _, newVal ->
                            waterIntakeParams.amount = newVal.toMl(
                                OZ_US
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }


        }
    }

    private fun addListener() {
        male_cv.setOnClickListener {
            changeColorCheckBox(male_cv, male_tv, male_cb, true)
            changeColorCheckBox(female_cv, female_tv, female_cb, false)
            mainViewModel.setSex(WaterIntakeParams.MALE)
        }
        female_cv.setOnClickListener {
            changeColorCheckBox(male_cv, male_tv, male_cb, false)
            changeColorCheckBox(female_cv, female_tv, female_cb, true)
            mainViewModel.setSex(WaterIntakeParams.FEMALE)
        }
        weight_picker.setOnValueChangedListener { picker, oldVal, newVal ->
            mainViewModel.setWeight(newVal)
        }
        waterIntakeParams.apply {
            physical_activity_bt.apply {
//                text = when(activity){
//                    SEDENTARY -> listPhysicalActivity[0]
//                    LIGHT_ACTIVITY -> listPhysicalActivity[1]
//                    ACTIVE -> listPhysicalActivity[2]
//                    else -> listPhysicalActivity[3]
//                }
                setOnClickListener {
                    BaseBottomSheetDialogFragment(
                        getString(R.string.choose_activity),
                        this@WaterIntakeFragment
                    ).run {
                        onDialogShow = {
                            setContentView(
                                SelectedBottomSheetFragment(
                                    listPhysicalActivity,
                                    listDetailPhysicalActivity,
                                    when (mainViewModel.activity.value) {
                                        SEDENTARY -> 0
                                        LIGHT_ACTIVITY -> 1
                                        ACTIVE -> 2
                                        else -> 3
                                    },
                                    this
                                ).apply {
                                    confirm = {
                                        mainViewModel.setActivity(WaterIntakeParams.listPhysical[it])
                                    }
                                }
                            )
                        }
                        show()
                    }

                }
            }
            climate_bt.apply {
//                text = when (weather) {
//                    COLD -> listClimate[0]
//                    TEMPERATE -> listClimate[1]
//                    else -> listClimate[2]
//                }
                setOnClickListener {
                    BaseBottomSheetDialogFragment(
                        getString(R.string.choose_climate),
                        this@WaterIntakeFragment
                    ).run {
                        onDialogShow = {
                            setContentView(
                                SelectedBottomSheetFragment(
                                    listClimate,
                                    listDetailClimate,
                                    when (mainViewModel.weather.value) {
                                        COLD -> 0
                                        TEMPERATE -> 1
                                        else -> 2
                                    },
                                    this
                                ).apply {
                                    confirm = {
                                        mainViewModel.setWeather(WaterIntakeParams.listClimate[it])
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
