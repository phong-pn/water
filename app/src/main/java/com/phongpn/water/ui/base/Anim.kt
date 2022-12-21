package com.phongpn.water.ui.base

/**
 * Class provider data for method setCustomAnimations() in FragmentTransaction
 * @see BaseActivity.toBaseFragment
 */
class Anim(var enter: Int, var exit: Int, var popEnter: Int, var popExit: Int ){
    fun makeAnim(anim: Anim.() -> Unit): Anim{
        anim()
        return this
    }
}
