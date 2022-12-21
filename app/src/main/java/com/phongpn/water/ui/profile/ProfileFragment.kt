package com.phongpn.water.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.phongpn.water.R
import com.phongpn.water.ui.MainActivity
import com.phongpn.water.ui.base.BaseFragment
import com.phongpn.water.util.constant.params.ML
import com.phongpn.water.util.constant.params.OZ_UK
import com.phongpn.water.util.constant.params.OZ_US
import com.phongpn.water.util.formatDrinkUnit
import com.phongpn.water.util.profileparams.UnitParams
import com.phongpn.water.util.profileparams.WaterIntakeParams
import com.phongpn.water.util.widget.GradientBackgroundTextView
import kotlinx.android.synthetic.main.expand_unit_layout.*
import kotlinx.android.synthetic.main.profile_fragment.*

class ProfileFragment : BaseFragment() {
    var canBackToMainFragment = true
    private var detailProfileFragment : BaseDetailProfileFragment? = null
    private val unitParams = UnitParams.getInstance()
    private val waterIntakeParams = WaterIntakeParams.getInstance()
    override fun onBackPressed() {
        if (canBackToMainFragment) {
            (requireActivity() as MainActivity).toHomeFragment {
                enter = R.anim.right_to_left
            }
        }
        else {
            detailProfileFragment?.backProfileFragment()
            canBackToMainFragment = true
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.profile_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        list_unit.apply {
            for (index in 0 until childCount){
                val gradientTextViewChild = getChildAt(index) as GradientBackgroundTextView
                gradientTextViewChild.apply {
                    isChoose = when (unitParams.unitDrink) {
                        ML -> index == 0
                        OZ_UK -> index == 2
                        else -> index == 1
                    }
                    onClickListener = {
                        if (isChoose) {
                            unitParams.unitDrink = when (index) {
                                0 -> ML
                                1 -> OZ_US
                                else -> OZ_UK
                            }
                        }
                        changeColorUnChooseUnitItem(this)
                    }
                }
            }
        }
        back_bt.setOnClickListener { onBackPressed() }
        water_intake_frame.setOnClickListener {
            goToDetailProfileFragment(WaterIntakeFragment(getString(R.string.my_daily_water_intake)))
        }
        notification_frame.setOnClickListener {
            goToDetailProfileFragment(NotificationFragment(getString(R.string.notification)))

        }
        settings_frame.setOnClickListener {
            goToDetailProfileFragment(SettingFragment(getString(R.string.settings)))
        }
        themes_frame.setOnClickListener {
            Toast.makeText(context, getString(R.string.coming_soon), Toast.LENGTH_SHORT).show()
        }
        sychronization_frame.setOnClickListener {
            Toast.makeText(context, getString(R.string.coming_soon), Toast.LENGTH_SHORT).show()
        }
        contact_us_frame.setOnClickListener {
            Toast.makeText(context, getString(R.string.coming_soon), Toast.LENGTH_SHORT).show()
        }
        privacy_policy_frame.setOnClickListener {
            Toast.makeText(context, getString(R.string.coming_soon), Toast.LENGTH_SHORT).show()
        }
        unitParams.observe { type, _ ->
            if (type == UnitParams.UNIT_DRINK){
                list_unit.apply {
                    for (index in 0 until childCount){
                        val gradientTextViewChild = getChildAt(index) as GradientBackgroundTextView
                        gradientTextViewChild.apply {
                            isChoose = when (unitParams.unitDrink) {
                                ML -> index == 0
                                OZ_UK -> index == 2
                                else -> index == 1
                            }
                        }
                    }
                }
                water_intake_amount_tv.text = formatDrinkUnit(waterIntakeParams.amount, ML)
            }
        }
        water_intake_amount_tv.text = formatDrinkUnit(waterIntakeParams.amount, ML)
        waterIntakeParams.observe { type, _ ->  if (type == WaterIntakeParams.AMOUNT)
            water_intake_amount_tv.text = formatDrinkUnit(waterIntakeParams.amount, ML)
        }
    }

    private fun goToDetailProfileFragment(fragment: BaseDetailProfileFragment){
        canBackToMainFragment = false
        detailProfileFragment = fragment.also {
            it.onBackProfileFragment = {
                title_profile_fragment.text = getString(R.string.my_profile)
                list_profile_item.visibility = View.VISIBLE
                canBackToMainFragment = true

            }
        }
        childFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.right_to_left, R.anim.left_to_right)
            .replace(R.id.profile_fragment_body, detailProfileFragment!!, null)
            .commit()
        list_profile_item.visibility = View.GONE
        title_profile_fragment.text = fragment.title
    }

    private fun changeColorUnChooseUnitItem(view : View) {
        list_unit.apply {
            for (index in 0 until childCount){
                val child = getChildAt(index)
                if (child != view){
                    (child as GradientBackgroundTextView).isChoose = false
                }
            }
        }
    }
}