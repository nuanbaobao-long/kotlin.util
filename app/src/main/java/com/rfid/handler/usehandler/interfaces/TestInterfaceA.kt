package com.rfid.handler.usehandler.interfaces

import com.rfid.handler.usehandler.Main2Activity
import com.rfid.handler.usehandler.bean.*
import dagger.Component

interface TestInterfaceA {
    fun test1(){}
    fun test2()
}

interface TestInterfaceB {
    fun test1(){}
    fun test2()
}

@Component(modules = [StudentModule::class,SchooleMoudle::class])
interface StudentInfomation{
    fun Infomation():Student
//    fun addSchool(schoole: Schoole)
}
