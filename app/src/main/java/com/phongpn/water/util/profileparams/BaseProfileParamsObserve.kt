package com.phongpn.water.util.profileparams

import com.google.gson.annotations.Expose

open class BaseProfileParamsObserve {
    @Transient
    var observers: MutableList<(String, Any) -> Unit> = mutableListOf()

    fun notifyDataChanged(type : String, data : Any) {
        try {
            observers.forEach {it.invoke(type, data)}
        }
        catch (e : Exception){
            e.printStackTrace()
        }
    }

    fun observe(observer: (String, Any) -> Unit){
        observers += observer
    }

    fun detachObservers() {
        observers = mutableListOf()
    }
}