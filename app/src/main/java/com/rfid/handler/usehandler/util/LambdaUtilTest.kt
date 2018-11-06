package com.rfid.handler.usehandler.util

import android.os.Handler
import android.util.Log
import com.rfid.handler.usehandler.bean.MessageBean

class LambdaUtilTest {
    companion object {
        fun <T, R> Collection<T>.flod(initial: R, combine: (acc: R, nextElement: T) -> R): String {
            var accumulator: R = initial
            for (element: T in this) {
                accumulator = combine(accumulator, element)
            }
            return accumulator.toString()
        }

    }
}

fun <E, T> List<E>.addText(initial: T, comm: (E, text: T) -> E): List<E> {
    var result = ArrayList<E>()
    for (item in this)
        result.add(comm(item, initial))
    return result
}

fun <E, T> List<E>.addText2(text: T, comms: (E, text: T) -> Unit) {
    for (item in this)
        comms(item, text)
}

fun <E, T, I> List<E>.addText3(text: T, i: I, comma: (E, text: T) -> Unit, comms: (E, i: I) -> Unit) {
    for (item in this) {
        comma(item, text)
        comms(item, i)
    }
}

fun <T> List<MessageBean>.addText4(comma: () -> T) {
    for (item in this)
        item.text += comma()
}

fun <T, I> List<T>.addText5(i: I, comm: T.(i: I) -> T) {
    for (item in this)
        item.comm(i)//也可以这样写
    //comm(item,i)
}

fun <T, I, S> List<T>.addText6(i: I, s: S, comm: T.(I)->(T) ->(I,S)->S.(I)-> T) {
    for (item in this) {//上面写的I.(S),只能写成(i,s),只有最前面的第一个参数可以写成T.comm(i).后面的直接在后面加()就行
        (item.comm(i))(item)(i,s)(s,i)//如果这边是无反的数据,可以只写前面一点就行了,使用那里写完全部动作就行,如果是有反的话,就需要写完,不然你写到那儿就只返回你写的那儿的定义的数据
    }
}

/** 内联函数*/
inline fun Handler.run(crossinline body:()->Unit){
    val r = object : Runnable{
        override fun run() = body()
    }
    this.postDelayed(r,1000)
}