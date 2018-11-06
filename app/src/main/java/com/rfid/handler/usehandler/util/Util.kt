package com.rfid.handler.usehandler.util

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.os.IBinder
import android.os.Parcelable
import android.support.v4.app.Fragment
import android.util.Log
import android.util.Size
import android.util.SizeF
import android.util.SparseArray
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.rfid.handler.usehandler.base.BaseApplication
import com.rfid.handler.usehandler.view.GlideRoundTransform
import java.io.Serializable

inline fun <reified T> Activity.startActivity2(vararg data: Any = arrayOf()) {
    startActivity<T>(*data)
}

/**
 * 跳转activity,传递的参数都是放在intent里面的
 * 键值从key0开始,key1,key2.....keyn
 */
inline fun <reified Activity> Fragment.startActivity(vararg data: Any = arrayOf()) {
    context?.startActivity<Activity>(*data)
}

/**
 * 跳转activity,传递的参数都是放在intent里面的
 * 键值从key0开始,key1,key2.....keyn
 */
inline fun <reified Activity> Context.startActivity(vararg data: Any = arrayOf()) {
    var toActivity = Activity::class.java
    var intent = Intent(this, toActivity)
    intent.addIntent(*data)
    for (i in data.indices) {
    }
    startActivity(intent)
}


/**
 * 将数据添加进入Intent,所有的键都是以key开头,后面跟0..~
 * 比如key0,key1;
 *
 */
inline fun Intent.addIntent(vararg data: Any) {
    for (i in data.indices) {
        val key = "key" + i.toString()
        var any = data[i]
        if (any is Int)
            this.putExtra(key, any)
        else if (any is Double)
            this.putExtra(key, any)
        else if (any is String)
            this.putExtra(key, any)
        else if (any is Boolean)
            this.putExtra(key, any)
        else if (any is ArrayList<*>)
            this.putExtra(key, any)
        else if (any is Array<*>)
            this.putExtra(key, any)
        else if (any is Short)
            this.putExtra(key, any)
        else if (any is Float)
            this.putExtra(key, any)
        else if (any is Long)
            this.putExtra(key, any)
        else if (any is Byte)
            this.putExtra(key, any)
        else if (any is Bundle)
            this.putExtras(any)
        else if (any is ByteArray)
            this.putExtra(key, any)
        else if (any is Char)
            this.putExtra(key, any)
        else if (any is IntArray)
            this.putExtra(key, any)
        else if (any is CharArray)
            this.putExtra(key, any)
        else if (any is DoubleArray)
            this.putExtra(key, any)
        else if (any is BooleanArray)
            this.putExtra(key, any)
        else if (any is FloatArray)
            this.putExtra(key, any)
        else if (any is Parcelable)
            this.putExtra(key, any)
        else if (any is LongArray)
            this.putExtra(key, any)
        else if (any is Serializable)
            this.putExtra(key, any)
        else
            throw CustomizeException("some type cannot be deposited in Intent,please check your data")
    }
}

/**
 * 根据传过来的值绑定进入Bundle
 *
 * 注意!!!!
 *
 * 返回的bundle的键都是以key开头,后面跟0..~的数字
 * 比如bundle.putString(key0,value)
 * 所以传递过来的bundle的键不能以key number命名
 *
 */
fun Array<Any>.toBundle(): Bundle {
    var bundle = Bundle()
    for (i in 0 until this.size) {
        val key = "key" + i.toString()
        val any = this[i]
        if (any is Int)
            bundle.putInt(key, any)
        else if (any is Double)
            bundle.putDouble(key, any)
        else if (any is Serializable)
            bundle.putSerializable(key, any)
        else if (any is String)
            bundle.putString(key, any)
        else if (any is Boolean)
            bundle.putBoolean(key, any)
        else if (any is Short)
            bundle.putShort(key, any)
        else if (any is Float)
            bundle.putFloat(key, any)
        else if (any is Long)
            bundle.putLong(key, any)
        else if (any is Byte)
            bundle.putByte(key, any)
        else if (any is Bundle)
            bundle.putAll(any)
        else if (any is IBinder)
            bundle.putBinder(key, any)
        else if (any is ByteArray)
            bundle.putByteArray(key, any)
        else if (any is Char)
            bundle.putChar(key, any)
        else if (any is IntArray)
            bundle.putIntArray(key, any)
        else if (any is CharArray)
            bundle.putCharArray(key, any)
        else if (any is DoubleArray)
            bundle.putDoubleArray(key, any)
        else if (any is BooleanArray)
            bundle.putBooleanArray(key, any)
        else if (any is ShortArray)
            bundle.putShortArray(key, any)
        else if (any is SparseArray<*>)
            bundle.putSparseParcelableArray(key, any as SparseArray<Parcelable>)
        else if (any is Size)
            bundle.putSize(key, any)
        else if (any is SizeF)
            bundle.putSizeF(key, any)
        else if (any is FloatArray)
            bundle.putFloatArray(key, any)
        else if (any is Parcelable)
            bundle.putParcelable(key, any)
        else if (any is LongArray)
            bundle.putLongArray(key, any)
        else if (any is CharSequence)
            bundle.putCharSequence(key, any)
        else if (any is ArrayList<*>)
            if (any.size > 0) {
                if (any[0] is String)
                    bundle.putStringArrayList(key, any as java.util.ArrayList<String>)
                else if (any[0] is Int)
                    bundle.putIntegerArrayList(key, any as java.util.ArrayList<Int>)
                else if (any[0] is Parcelable)
                    bundle.putParcelableArrayList(key, any as java.util.ArrayList<Parcelable>)
                else if (any[0] is CharSequence)
                    bundle.putCharSequenceArrayList(key, any as java.util.ArrayList<CharSequence>)
                else
                    throw CustomizeException("bundle just add StringArrayList,IntegerArrayList,ParcelableArrayList,CharSequenceArrayList")
            } else
                throw CustomizeException("bundle cannot add list by size=0")
        else if (any is Array<*>)
            if (any.size > 0) {
                if (any[0] is String)
                    bundle.putStringArray(key, any as Array<String>)
                else if (any[0] is Parcelable)
                    bundle.putParcelableArray(key, any as Array<Parcelable>)
                else if (any[0] is CharSequence)
                    bundle.putCharSequenceArray(key, any as Array<CharSequence>)
                else
                    throw CustomizeException("bundle just add StringArray,ParcelableArray")
            } else
                throw CustomizeException("bundle cannot add array by size=0")
    }
    return bundle
}

fun Int.showDialog(context: Context, sure: (() -> Unit)? = null, cancle: (() -> Unit?)? = null, sureText: String = "确定", cancleText: String = "取消", titleText: String = "温馨提示") {
    context.resources.getString(this).showDialog(context, sure, cancle, sureText, cancleText)
}

fun String.showDialog(context: Context, sure: (() -> Unit)? = null, cancle: (() -> Unit?)? = null, sureText: String = "确定", cancleText: String = "取消", titleText: String = "温馨提示"):AlertDialog.Builder{
    var dialog = AlertDialog.Builder(context)
    dialog.setTitle(titleText).setMessage(this)
    if (null != sure)
        dialog.setNegativeButton(sureText, object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, which: Int) {
                sure()
            }
        })
    if (null != cancle)
        dialog.setNeutralButton(cancleText, object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, which: Int) {
                cancle()
            }
        })
    dialog.create().show()
    return dialog
}

fun ImageView.loadUrl(url: String) {
    Glide.with(context).load(url).centerCrop().transform(GlideRoundTransform(context)).crossFade(300).into(this)
}

fun String.l() {
    Log.e("LOG_TAG", this)
}

class Util {
    companion object {
        @SuppressLint("MissingPermission")
        @JvmStatic
        fun HasNetWrok(): Boolean {
            val connectivityManager = BaseApplication.getContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val info = connectivityManager.activeNetworkInfo
            if (info != null && info.isAvailable)
                return true
            return false
        }
    }
}
