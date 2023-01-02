package com.phongpn.water.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.phongpn.water.R
import com.phongpn.water.ui.MainActivity
import com.phongpn.water.ui.base.BaseFragment
import com.phongpn.water.util.profileparams.WaterIntakeParams
import com.phongpn.water.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.profile_fragment.*

class ProfileFragment : BaseFragment() {
    var canBackToMainFragment = true
    private var detailProfileFragment: BaseDetailProfileFragment? = null
    private val mainViewModel by activityViewModels<MainViewModel>()
    override fun onBackPressed() {
        if (canBackToMainFragment) {
            (requireActivity() as MainActivity).toHomeFragment {
                enter = R.anim.right_to_left
            }
        } else {
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
        addListener()
        addObserver()


    }

    private fun addObserver() {
        mainViewModel.goal.observe(viewLifecycleOwner) {
            water_intake_amount_tv.text = "$it ml"
        }
    }

    private fun addListener() {
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
    }

    private fun goToDetailProfileFragment(fragment: BaseDetailProfileFragment) {
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
}