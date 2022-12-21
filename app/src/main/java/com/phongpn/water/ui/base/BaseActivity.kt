package com.phongpn.water.ui.base

import android.os.Bundle
import android.os.PersistableBundle
import androidx.annotation.IdRes
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainer
import androidx.fragment.app.FragmentTransaction
import com.phongpn.water.ui.home.HomeFragment

open class BaseActivity: AppCompatActivity() {
    @IdRes var container = 0

    fun FragmentTransaction.setCustomAnimation(anim: Anim.() -> Unit): FragmentTransaction {
        Anim(0, 0, 0,0).makeAnim(anim).apply {
            setCustomAnimations(enter, exit, popEnter, popExit)
        }
        return this
    }

    inline fun <reified T: BaseFragment> toBaseFragment(noinline anim: (Anim.() -> Unit)? = null) {
        supportFragmentManager.beginTransaction().apply {
            getFragmentShowing()?.let { showingFragment ->
                hide(showingFragment) }

            anim?.let { setCustomAnimation(it) }
            getBaseFragment<T>()?.let {
                show(it)
            } ?: add(container, T::class.java, null)
            commit()
        }
    }

    inline fun <reified T: BaseFragment> getBaseFragment(): T? {
        supportFragmentManager.fragments.forEach {
            if (it is T) return it
        }
        return null
    }

    fun getFragmentShowing(): Fragment? {
        supportFragmentManager.fragments.forEach { if (it.isVisible && it.isResumed ) return it }
        return null
    }

    /**
     * Go to home fragment. If a instance of HomeFragment was initialized before and is showing, return false.
     * Else, show HomeFragment, then return true
     */
     fun toHomeFragment(anim: (Anim.() -> Unit)? = null): Boolean {
        if (getFragmentShowing() is HomeFragment) return false
        toBaseFragment<HomeFragment>(anim)
        return true
     }
}