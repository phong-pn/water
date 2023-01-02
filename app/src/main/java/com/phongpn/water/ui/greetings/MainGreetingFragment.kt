package com.phongpn.water.ui.greetings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.phongpn.water.R
import com.phongpn.water.storage.SharePrefUtil
import com.phongpn.water.ui.MainActivity
import kotlinx.android.synthetic.main.main_greeting_fragment.*

class MainGreetingFragment : Fragment() {
    private val viewModel: GreetingViewModel by activityViewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.main_greeting_fragment, container, false)
    }

    private val fragments = listOf(
        FirstGreetingFragment(),
        TakeParamsFragment(),
        DrinkTypeFragment(),
        SetupNotificationFragment()
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        next_greeting_fragment_bt.setOnClickListener {
            greeting_fragment_viewpager2.apply {
                if (currentItem != fragments.size - 1) setCurrentItem(currentItem + 1, true)
                else {
                    SharePrefUtil.firstLaunch = false
                    (fragments[currentItem] as SetupNotificationFragment).schedule()
                    requireActivity().apply {
                        startActivity(Intent(this, MainActivity::class.java))
                        finish()
                    }
                }
            }
        }
        greeting_fragment_viewpager2.apply {
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    changeIndicator(position)
                    next_greeting_fragment_bt.text =
                        if (position == fragments.size - 1) context.getString(R.string.done)
                        else getString(R.string.next)

                }
            })
            adapter = GreetingFragmentsAdapter()
        }
    }

    private fun changeIndicator(position: Int) {
        for (index in 0 until indicator_greeting_fragments.childCount) {
            indicator_greeting_fragments.getChildAt(index).apply {
                if (index == position) setBackgroundResource(R.drawable.indicator_selected)
                else setBackgroundResource(R.drawable.indicator_normal)
            }
        }
    }

    inner class GreetingFragmentsAdapter : FragmentStateAdapter(this) {
        override fun getItemCount() = fragments.size

        override fun createFragment(position: Int): Fragment {
            return fragments[position]
        }

    }
}