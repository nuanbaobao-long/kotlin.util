package com.rfid.park.base

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.jcodecraeer.xrecyclerview.ProgressStyle
import com.jcodecraeer.xrecyclerview.XRecyclerView
import com.rfid.handler.usehandler.R
import com.rfid.handler.usehandler.base.BaseApplication
import com.rfid.handler.usehandler.base.BaseBean
import com.rfid.handler.usehandler.view.GlideRoundTransform
import io.reactivex.Observable
import org.greenrobot.eventbus.EventBus

abstract class BaseFragment : Fragment(),  XRecyclerView.LoadingListener,SwipeRefreshLayout.OnRefreshListener {

    protected var app: BaseApplication? = null
    lateinit var mRootView: View
    lateinit var apiManager: BaseApiManager
    lateinit var baseService: BaseService
    lateinit var baseShared: BaseShared
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mRootView = inflater.inflate(getContentView(), container, false)
        app = context!!.applicationContext as BaseApplication
        return mRootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        apiManager = BaseApiManager.instance()
        baseService = apiManager.getBaseService()
        baseShared = BaseShared(context!!)
        initView()
    }

    abstract fun getContentView(): Int

    abstract fun initView()

    protected abstract fun onClick(v:View?)

    protected open fun <T> getData(observable: Observable<BaseBean<T>>, baseRequest: BaseRequest<T>) {
        apiManager.getData(observable, baseRequest)
    }

    protected fun <T> Observable<BaseBean<T>>.setRequest(baseRequest: BaseRequest<T>) {
        getData(this, baseRequest)
    }

    protected fun ImageView.loadUrl(url: String) {
        Glide.with(context).load(url).centerCrop().transform(GlideRoundTransform(context)).crossFade(300).into(this)
    }

    fun initSwipeRefreshLayout(sr_: SwipeRefreshLayout) {
        //设置下拉出现小圆圈是否是缩放出现，出现的位置，最大的下拉位置
        sr_.setProgressViewOffset(true, 0, 100)
        //设置下拉圆圈的大小，两个值 LARGE， DEFAULT
        sr_.setSize(SwipeRefreshLayout.DEFAULT)
        sr_.setColorSchemeResources(R.color.colorPrimaryDark)
    }

    fun initBaseRecyclerView(recyclerView: XRecyclerView,canRefresh:Boolean = false) {
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

    fun getLayoutManager(): RecyclerView.LayoutManager {
        var layoutmanager = LinearLayoutManager(context)
        layoutmanager.orientation = LinearLayoutManager.VERTICAL
        return layoutmanager
    }

    override fun onDestroyView() {
        super.onDestroyView()
        EventBus.getDefault().unregister(this)
    }

    protected fun exit() {
        app?.exit()
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

//    @SuppressLint("ResourceAsColor")
//    protected fun setTitle(title: String, txtColor: Int = R.color.txt_title_color) {
//        tv_title.setText(title)
//        tv_title.setTextColor(resources.getColor(txtColor))
//    }
//
//    protected fun String.title(txtColor: Int = R.color.txt_title_color) {
//        setTitle(this, txtColor)
//    }
//
//    protected fun Int.title(txtColor: Int = R.color.txt_title_color) {
//        setTitle(getString(this), txtColor)
//    }
//
//    protected fun showBack(resoure: Int?) {
//        resoure.let { iv_back.setImageResource(resoure!!) }
//        iv_back.visibility = View.VISIBLE
//        iv_back.setOnClickListener { activity!!.finish() }
//    }
//
//    protected fun showBack() {
//        showBack(null)
//    }

    var toast: Toast? = null
    protected fun toast(msg: Int) {
        toast(resources.getString(msg))
    }

    protected fun toast(msg: String) {
        toast = toast ?: Toast.makeText(context, msg, Toast.LENGTH_SHORT)
        toast?.setText(msg)
        toast?.show()
    }

    protected fun String?.showToast() {
        if (this != null)
            toast(this)
    }

    protected fun Int.showToast() {
        toast(this)
    }

    protected fun <T> startactivity(activity: Class<T>) {
        startActivity(Intent(context, activity))
    }

    protected fun <T> startactivity(activity: Class<T>, bundle: Bundle) {
        startActivity(Intent(context, activity).putExtra("bundle", bundle))
    }
}