package com.rfid.park.base

import com.rfid.handler.usehandler.base.BaseBean
import io.reactivex.Observable
import okhttp3.MultipartBody
import retrofit2.http.*

interface BaseService {

    /** 常用写法*/
    @POST("login")
    @FormUrlEncoded
    fun login(@Field("login_name") login_name: String, @Field("code") code: String): Observable<BaseBean<String>>

    /** 直接使用url,不传值*/
    @DELETE("{action}")
    fun delete(@Path(value = "action", encoded = true) action: String): Observable<BaseBean<String>>

    /**上传图片*/
    @Multipart
    @POST("face-avatar/upload")
    fun upload(@Part img: MultipartBody.Part): Observable<BaseBean<String>>
}