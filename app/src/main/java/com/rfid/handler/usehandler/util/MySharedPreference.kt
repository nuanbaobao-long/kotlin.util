package com.rfid.handler.usehandler.util

import android.content.Context
import java.lang.IllegalArgumentException
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

/***
 * 这个方法只能activity里面能用,因为fragment在定义值的时候哈没有初始化context
 */
class MySharedPreference<T>(val context: Context?, val key: String, val default: T) : ReadWriteProperty<Any?, T> {


    override fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return getValue()
    }

    override fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        setValue(value)
    }

    private fun <T> getValue(): T {
        /** 初始化sharedPreference,用lazy委托*/
        if (context == null)
            return "0" as T
        val preference by lazy {
            context!!.getSharedPreferences("MySharedPreference", Context.MODE_PRIVATE)
        }
        return with(preference) {
            when (default) {
                is Long -> getLong(key, default)
                is String -> getString(key, default)
                is Int -> getInt(key, default)
                is Boolean -> getBoolean(key, default)
                is Float -> getFloat(key, default)
                else -> throw IllegalArgumentException("This type cannot get from Preferences")
            } as T
        }
    }

    private fun <T> setValue(value: T) {
        if (context == null)
            return
        /** 初始化sharedPreference,用lazy委托*/
        val preference by lazy {
            context!!.getSharedPreferences("MySharedPreference", Context.MODE_PRIVATE)
        }
        return with(preference.edit()) {
            when (value) {
                is Long -> putLong(key, value)
                is String -> putString(key, value)
                is Int -> putInt(key, value)
                is Boolean -> putBoolean(key, value)
                is Float -> putFloat(key, value)
                else -> throw IllegalArgumentException("This type cannot be saved to Preferences")
            }.commit()
        }
    }

}