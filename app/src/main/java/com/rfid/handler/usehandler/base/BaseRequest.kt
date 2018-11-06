package com.rfid.park.base

import com.rfid.handler.usehandler.base.BaseBean
import com.rfid.handler.usehandler.base.BaseBeans

interface BaseRequest<T> {
    fun Success(data: BaseBean<T>?)
    fun faild(errorMsg: String?)
}

interface BaseRequests<T> {
    fun Success(data: BaseBeans<T>?)
    fun faild(errorMsg: String?)
}

interface OnItemClickListener<T> {
    fun ItemClick(bean: T)
}

interface OnChildClickListener<T> {
    fun ChildClick(bean: T)
}