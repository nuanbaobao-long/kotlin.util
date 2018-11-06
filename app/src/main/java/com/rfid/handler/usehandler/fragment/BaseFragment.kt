package com.rfid.handler.usehandler.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.IBinder
import android.os.Parcelable
import android.support.design.internal.ParcelableSparseArray
import android.support.v4.app.Fragment
import android.util.Size
import android.util.SizeF
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.rfid.handler.usehandler.R
import com.rfid.handler.usehandler.util.CustomizeException
import java.io.Serializable

abstract class BaseFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(getContentView(), container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    abstract fun initView()

    abstract fun getContentView(): Int

    fun Int.showToast() {
        getString(this).showToast()
    }

    fun String.showToast() {
        if (toast == null) {
            toast = Toast.makeText(context, this, Toast.LENGTH_SHORT)
        } else {
            toast?.setText(this)
        }
        toast?.show()
    }

    private var toast: Toast? = null
}
