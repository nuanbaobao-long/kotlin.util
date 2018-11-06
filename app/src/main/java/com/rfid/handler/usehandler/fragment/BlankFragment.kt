package com.rfid.handler.usehandler.fragment

import android.content.Context
import android.graphics.Camera
import android.hardware.camera2.CameraManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.SurfaceHolder
import android.view.View
import android.view.ViewGroup
import android.widget.Toast

import com.rfid.handler.usehandler.R
import com.rfid.handler.usehandler.util.PermissionUtil
import kotlinx.android.synthetic.main.fragment_blank.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragment must implement the
 * [BlankFragment.OnFragmentInteractionListener] interface
 * to handle interaction events.
 * Use the [BlankFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 */
class BlankFragment : Fragment(), SurfaceHolder.Callback, View.OnClickListener, Handler.Callback {
    override fun handleMessage(msg: Message?): Boolean {
        when (msg?.what) {
            1 -> {
                ChildHandler?.sendEmptyMessage(1)
            }
            0 -> {
                showToast("从主线程到子线程再到主线程的传递")
                ChildHandler?.sendEmptyMessage(2)
            }
            2 -> {
//                        try {
                ChildHandler?.sendEmptyMessage(0)
                //这儿确实是发不出来了,但是却也没有走异常,因为messagequeue中并没有抛出异常,只是提示当前handler sending message to a Handler on a dead thread
//                        }catch (e:Exception){
//                            showToast("因为子线程的Loop被关闭了,这里是异常")
//                        }
            }
        }
        return true
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.button -> UIHandler?.sendEmptyMessageDelayed(1, 1000)
            R.id.permission -> PermissionUtil.RequestAllPermission(activity)
            R.id.btn_open -> {
                cameraManager?.setTorchMode("0", !openState)
                openState = !openState
                btn_open.setText(if (openState) "关闭" else "开启")
            }
        }
    }

    var openState = false


    override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) {
    }

    override fun surfaceDestroyed(holder: SurfaceHolder?) {
    }

    override fun surfaceCreated(holder: SurfaceHolder?) {
    }

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null
    private var cameraManager: CameraManager? = null
    private var _camera: Camera? = null
    var UIHandler: Handler? = null
    var ChildHandler: Handler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_blank, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    var surfaceHolder: SurfaceHolder? = null
    private fun initView() {
        cameraManager = activity!!.getSystemService(Context.CAMERA_SERVICE) as CameraManager?
        permission.setOnClickListener(this)
        button.setOnClickListener(this)
        btn_open.setOnClickListener(this)
        surfaceHolder = sv.holder
        surfaceHolder?.addCallback(this)
        surfaceHolder?.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)
        UIHandler = Handler(this)
        UIHandler = object : Handler() {
            override fun handleMessage(msg: Message?) {
                super.handleMessage(msg)
            }
        }
        Thread(Runnable {
            Looper.prepare()
            ChildHandler = object : Handler() {
                override fun handleMessage(msg: Message?) {
                    super.handleMessage(msg)
                    when (msg?.what) {
                        1 -> {
                            UIHandler?.sendEmptyMessage(0)
                        }
                        2 -> {
                            ChildHandler?.looper?.quit()
                        }
                        0 -> {
                            UIHandler?.post(Runnable { showToast("居然没有异常,到了这儿") })
                        }
                    }
                }
            }
            Looper.loop()
            UIHandler?.sendEmptyMessage(2)
        }).start()

        var handler = object : Handler() {
            override fun handleMessage(msg: Message?) {
                super.handleMessage(msg)
                when (msg?.what) {
                    0 -> activity?.runOnUiThread { showToast("子线程的数据") }
                }
            }
        }
    }

    var toast: Toast? = null
    fun showToast(msg: Any) {
        var m = ""
        if (msg is String) {
            m = msg
        } else if (msg is Int) {
            m = resources.getString(msg)
        }
        if (null == toast) {
            toast = Toast.makeText(context, m, Toast.LENGTH_SHORT)
            toast?.show()
        } else {
            toast?.setText(m)
            toast?.show()
        }

    }

    // TODO: Rename method, update argument and hook method into UI event
    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener") as Throwable
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
        UIHandler?.removeCallbacksAndMessages("")
        ChildHandler?.removeCallbacksAndMessages("")
    }

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
                BlankFragment().apply {
                    arguments = Bundle().apply {
                    }
                }
    }
}
