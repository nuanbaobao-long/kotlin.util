package com.rfid.park.base

import android.content.Intent
import android.text.TextUtils
import android.util.Log
import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.JSONObject
import com.google.gson.Gson
import com.rfid.handler.usehandler.base.BaseApplication
import com.rfid.handler.usehandler.base.BaseBean
import com.rfid.handler.usehandler.base.BaseBeans
import com.rfid.handler.usehandler.util.Util
import com.rfid.handler.usehandler.util.l
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import io.reactivex.Observer
import io.reactivex.functions.Function
import okhttp3.Interceptor
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

class BaseApiManager {

    companion object {
        val BASE_URL = "http://120.77.219.198:8906/app/v1/"
        val BASE_UPLOAD_URL = "http://120.77.219.198:8906/file/"
        private var apiManager: BaseApiManager? = null
        private lateinit var baseService: BaseService

        fun instance(): BaseApiManager {
            if (null == apiManager) {
                synchronized(BaseApiManager.javaClass) {
                    if (null == apiManager) {
                        apiManager = BaseApiManager()
                        baseService = apiManager!!.getRetrofit().create(BaseService::class.java)
                    }
                }
            }
            return apiManager!!
        }
    }

    private val apiMonitor = Any()

    fun <T> getDatas(observable: Observable<BaseBeans<T>>, onRequest: BaseRequests<T>) = if (Util.HasNetWrok()) {
        var observer = object : Observer<BaseBeans<T>> {
            override fun onSubscribe(d: Disposable) {
            }

            override fun onNext(t: BaseBeans<T>) {
                if (t.meta == 10000) {
                    BaseApplication.getContext().exit()//退出全部activity,并且跳转到登陆界面
                } else if (t.meta != 0) {
                    onRequest.faild(t.error)
                } else {
                    onRequest.Success(t)
                }
            }

            override fun onError(e: Throwable) {
                onRequest.faild(e.message)
            }

            override fun onComplete() {
            }
        }
        observable
                .map(object : Function<BaseBeans<T>, BaseBeans<T>> {
                    override fun apply(t: BaseBeans<T>): BaseBeans<T> {
                        return t
                    }

                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer)
    } else {
        onRequest.faild("not NetWrok")
    }

    fun <T> getData(observable: Observable<BaseBean<T>>, onRequest: BaseRequest<T>) = if (Util.HasNetWrok()) {
        var observer = object : Observer<BaseBean<T>> {
            override fun onSubscribe(d: Disposable) {
            }

            override fun onNext(t: BaseBean<T>) {
                if (t.meta == 10000) {
                    BaseApplication.getContext().exit()//关闭全部activity并跳转到登陆界面,这儿哈没写登陆
                } else if (t.meta != 0) {
                    onRequest.faild(t.error)
                } else {
                    onRequest.Success(t)
                }
            }

            override fun onError(e: Throwable) {
                onRequest.faild(e.message)
            }

            override fun onComplete() {
            }
        }
        observable
                .map(object : Function<BaseBean<T>, BaseBean<T>> {
                    override fun apply(t: BaseBean<T>): BaseBean<T> {
                        return t
                    }

                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer)
    } else {
        onRequest.faild("not NetWrok")
    }


    fun ResetBaseService() {
        baseService = getRetrofit().create(BaseService::class.java)
    }

    fun getBaseService(): BaseService {
        if (baseService == null) {
            synchronized(apiMonitor) {
                if (baseService == null) {
                    getRetrofit().create(BaseService::class.java)
                }
            }
        }
        return baseService!!
    }

    fun getUploadService() =
            getRetrofit(BASE_UPLOAD_URL).create(BaseService::class.java)

    fun getRetrofit(url: String = BASE_URL): Retrofit {
        return Retrofit.Builder().client(mClient).baseUrl(url).addCallAdapterFactory(RxJava2CallAdapterFactory.create()).addConverterFactory(GsonConverterFactory.create()).build()
    }

    private val interceptor = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
        override fun log(message: String?) {
            ("RequestCallBack=" + message).l()
        }
    })

    private val httpCacheDirectory = File(BaseApplication.getContext().cacheDir, "park")
    private val cacheSize = 10 * 1024 * 1024.toLong()
    private var cache = Cache(httpCacheDirectory, cacheSize)
    private val mClient = OkHttpClient.Builder()
            .addInterceptor {
                return@addInterceptor it.proceed(it.request().newBuilder().addHeader("X-Access-Token", BaseShared(BaseApplication.getContext()).get("token", "") as String).build())//这儿添加请求头
            }
            .addInterceptor(interceptor.setLevel(HttpLoggingInterceptor.Level.BODY))
            .connectTimeout(10, TimeUnit.SECONDS).readTimeout(10, TimeUnit.SECONDS).cache(cache).build()
}

