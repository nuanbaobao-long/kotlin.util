package com.rfid.handler.usehandler.base

import android.app.Activity
import android.app.Application

class BaseApplication : Application() {
    var token: String? = null
    var activitys = ArrayList<Activity>()
    override fun onCreate() {
        super.onCreate()
        context = this
    }


    companion object {
        private lateinit var context: BaseApplication

        fun getContext() = context
    }

    fun addActivity(activity: Activity) {
        activitys.add(activity)
    }

    fun exit() {
        for (i in 0..activitys.size-1) {
            activitys[i].finish()
        }
    }
}