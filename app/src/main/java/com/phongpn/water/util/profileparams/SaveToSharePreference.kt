package com.phongpn.water.util.profileparams

import android.content.Context

interface SaveToSharePreference {
    fun get(context: Context)
    fun save(context: Context)
}