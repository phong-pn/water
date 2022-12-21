package com.phongpn.water.ui.greetings

import androidx.fragment.app.Fragment
import com.phongpn.water.R
import com.phongpn.water.notification.AlarmSchedule
import com.phongpn.water.ui.profile.NotificationFragment

class FirstGreetingFragment : Fragment(R.layout.first_greeting_fragment)
class DrinkTypeFragment : Fragment(R.layout.drink_type_fragment)
class TakeParamsFragment : Fragment(R.layout.take_params_fragment)
class SetupNotificationFragment : Fragment(R.layout.set_up_notification_fragment){
    fun schedule(){
        AlarmSchedule.apply {
            set((childFragmentManager.fragments[0] as NotificationFragment).alarm)
            get().apply {
                if (active) schedule(context!!)
            }
        }
    }
}