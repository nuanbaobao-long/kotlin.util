package com.rfid.handler.usehandler.base

import java.io.Serializable

data class BaseBean<T>(var error: String, var meta: Int, var item: T?, var items: List<T?>, var token: String, var login_name: String, var md5: String, var md5_code: String, val total: Int)

data class BaseBeans<T>(var error: String, var meta: Int, var item: T?, var items: Array<T?>, var token: String, var login_name: String, var md5: String, var md5_code: String)