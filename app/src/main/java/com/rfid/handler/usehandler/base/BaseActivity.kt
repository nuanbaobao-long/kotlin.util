package com.rfid.park.base

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import com.jcodecraeer.xrecyclerview.ProgressStyle
import com.jcodecraeer.xrecyclerview.XRecyclerView
import com.rfid.handler.usehandler.R
import com.rfid.handler.usehandler.base.BaseApplication
import com.rfid.handler.usehandler.base.BaseBean
import io.reactivex.Observable
import org.greenrobot.eventbus.EventBus

abstract class BaseActivity : FragmentActivity(), SwipeRefreshLayout.OnRefreshListener, XRecyclerView.LoadingListener {


    protected var app: BaseApplication? = null
    protected lateinit var apiManager: BaseApiManager
    protected lateinit var baseService: BaseService
    protected lateinit var baseShared: BaseShared
    protected var bundle : Bundle?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initAfterContentView()
        bundle = intent.getBundleExtra("bundle")
        baseShared = BaseShared(baseContext)
        apiManager = BaseApiManager.instance()
        baseService = apiManager.getBaseService()
        app = application as BaseApplication
        app?.addActivity(this)
        setContentView(getContentView())
        initView()
    }

    protected fun setStatusBarColor(update: Boolean) {
        if (update) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        }
        window.decorView.findViewById<FrameLayout>(android.R.id.content).setPadding(0, 0, 0, 0);
    }

    /** 留给子类去实现*/
    protected open fun initAfterContentView() {

    }

    protected abstract fun onClick(v:View?)

    protected open fun <T> getData(observable: Observable<BaseBean<T>>, baseRequest: BaseRequest<T>) {
        apiManager.getData(observable, baseRequest)
    }

    protected fun <T> Observable<BaseBean<T>>.setRequest(baseRequest: BaseRequest<T>) {
        getData(this, baseRequest)
    }

    fun initBaseRecyclerView(recyclerView: XRecyclerView, canRefresh:Boolean = false) {
        recyclerView.layoutManager = getLayoutManager()
        if (canRefresh){
            recyclerView.setRefreshProgressStyle(ProgressStyle.Pacman)
            recyclerView.setLoadingMoreProgressStyle(ProgressStyle.BallRotate)
            recyclerView.setLoadingListener(this)
        }else{
            recyclerView.setPullRefreshEnabled(false)
            recyclerView.setLoadingMoreEnabled(false)
        }
    }

    fun initSwipeRefreshLayout(sr_: SwipeRefreshLayout) {
        //设置下拉出现小圆圈是否是缩放出现，出现的位置，最大的下拉位置
        sr_.setProgressViewOffset(true, 0, 100)
        //设置下拉圆圈的大小，两个值 LARGE， DEFAULT
        sr_.setSize(SwipeRefreshLayout.DEFAULT)
        sr_.setColorSchemeResources(R.color.colorPrimaryDark)
        sr_.setOnRefreshListener(this)
    }

    fun getLayoutManager(): RecyclerView.LayoutManager {
        var layoutmanager = LinearLayoutManager(this)
        layoutmanager.orientation = LinearLayoutManager.VERTICAL
        return layoutmanager
    }

    override fun onRefresh() {
        refresh()
    }

    override fun onLoadMore() {
        loadMore()
    }

    protected open fun refresh(){

    }

    protected open fun loadMore() {

    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }

    @SuppressLint("ResourceAsColor")
    protected fun setTitle(title: String, txtColor: Int = R.color.colorPrimary) {
//        tv_title.setText(title)
//        tv_title.setTextColor(resources.getColor(txtColor))
    }

    protected fun Int.title() {
        setTitle(resources.getString(this))
    }

    protected fun Int.title(txtColor: Int) {
        setTitle(getString(this), txtColor)
    }

//    protected fun showBack(resoure: Int = R.mipmap.icon_back) {
//        showBack(resoure, null)
//    }
//
//    protected fun showBack(resoure: Int = R.mipmap.icon_back, color: Int?) {
//        iv_back.setImageResource(resoure)
//        if (color != null)
//            iv_back.setColorFilter(color)
//        iv_back.visibility = View.VISIBLE
//        iv_back.setOnClickListener { finish() }
//    }
//
//    @JvmOverloads
//    protected fun showRight(resoure: String = getString(R.string.save), resoureInt: Int = R.string.save, onClickListener: View.OnClickListener) {
//        if (!resoure.equals(getString(R.string.save))) {
//            tv_save.setText(resoure)
//        } else if (resoureInt != R.string.save) {
//            tv_save.setText(resoureInt)
//        } else
//            tv_save.setText(resoure)
//        tv_save.visibility = View.VISIBLE
//        tv_save.setOnClickListener(onClickListener)
//    }
//
//    protected fun hideRight() {
//        tv_save.visibility = View.GONE
//    }

    abstract fun getContentView(): Int

    abstract fun initView()

    protected fun exit() {
        app?.exit()
    }

    var toast: Toast? = null
    protected fun toast(msg: Int?) {
        msg.let {
            toast(resources.getString(msg!!))
        }
    }

    protected fun toast(msg: String?) {
        toast = toast ?: Toast.makeText(this, msg, Toast.LENGTH_SHORT)
        toast?.setText(msg)
        toast?.show()
    }

    protected fun String?.showToast() {
        toast(this)
    }

    protected fun Int?.showToast() {
        toast(this)
    }
}