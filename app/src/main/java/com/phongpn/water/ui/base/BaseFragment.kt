package com.phongpn.water.ui.base

import androidx.fragment.app.Fragment
import com.phongpn.water.ui.MainActivity
import java.lang.Exception

abstract class BaseFragment : Fragment(){
    abstract fun onBackPressed()
}