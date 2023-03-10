package com.phongpn.water.ui

import android.content.Context
import android.os.Bundle
import android.os.PowerManager
import androidx.activity.addCallback
import com.phongpn.water.R
import com.phongpn.water.notification.AlarmSchedule
import com.phongpn.water.storage.Pref
import com.phongpn.water.ui.base.BaseActivity
import com.phongpn.water.ui.base.BaseFragment
import com.phongpn.water.ui.dialog.base.Modifier
import com.phongpn.water.ui.dialog.showExitDialogFragment
import com.phongpn.water.ui.home.HomeFragment

class MainActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        container = R.id.activity_main
        supportFragmentManager.beginTransaction()
            .replace(container, HomeFragment::class.java, null)
            .commit()
        onBackPressedDispatcher.addCallback {
            val fragmentShowing = getFragmentShowing()
            if (fragmentShowing !is HomeFragment) (fragmentShowing as BaseFragment).onBackPressed()
            else {
                showExitDialogFragment(
                    modifier = Modifier {
                        corner(20)
                    },
                    supportFragmentManager
                ) {
                    setPositionButtonOnClick {
                        finish()
                    }
                    setNegativeButtonOnClick {
                        dismiss()
                    }
                }

            }
        }
//        if (!checkDevicePowerAllow()) startActivity(Intent(android.provider.Settings.ACTION_IGNORE_BATTERY_OPTIMIZATION_SETTINGS))
    }
//

    private fun checkDevicePowerAllow() = (getSystemService(Context.POWER_SERVICE) as PowerManager)
        .isIgnoringBatteryOptimizations(packageName)

    override fun onPause() {
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        AlarmSchedule.get().detachObservers()
    }
}