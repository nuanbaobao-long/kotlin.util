package com.rfid.handler.usehandler.util

import java.lang.Exception

class CustomizeException : Exception{

    constructor(errorMessage:String):super(errorMessage)
    constructor(errorMessage: String,throwable: Throwable):super(errorMessage,throwable)
    constructor(throwable: Throwable):super(throwable)


}