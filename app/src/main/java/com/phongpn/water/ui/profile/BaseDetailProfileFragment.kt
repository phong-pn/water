package com.phongpn.water.ui.profile
import androidx.fragment.app.Fragment
import com.phongpn.water.R

open class BaseDetailProfileFragment(val title : String) : Fragment() {
    var onBackProfileFragment : (() -> Unit)? = null

    open fun backProfileFragment(){
        parentFragmentManager.beginTransaction()
            .remove(this)
            .runOnCommit {
                onBackProfileFragment?.invoke()
            }
            .commitNow()
    }


}