package com.rfid.handler.usehandler.bean

import dagger.Module
import dagger.Provides
import java.lang.StringBuilder
import java.lang.annotation.Documented
import java.lang.annotation.RetentionPolicy
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Qualifier

data class MessageBean(var id: Int, var text: String)

class Student @Inject constructor(){
    @field:[Inject Named("name")] lateinit var name: String
    @Inject lateinit var age : Number
    @field:[Inject Named("sex")] lateinit var sex: String
    @Inject lateinit var schoole: Schoole
}
class Schoole @Inject constructor(){
    @field:[Inject Named("schooleName")] lateinit var schooleName: String
    @field:[Inject Named("schooleAddress")] lateinit var schooleAddress: String
}
@Module
class StudentModule(var name: String,var age: Number,var sex: String,var schoole: Schoole){
    @Provides
    @Named("name")
    fun provideName() = name

    @Provides
    @Named("sex")
    fun provideSex() = sex

    @Provides
    fun provideAge() = age

    @Provides
    fun provideSchoole() = schoole
}

@Module
class SchooleMoudle(var schoole: Schoole){
    @Provides
    @Named("schooleName")
    fun providesSchooleName() = schoole.schooleName

    @Provides
    @Named("schooleAddress")
    fun provideSchooleAddress() = schoole.schooleAddress

}

@Module
class ActivityModule()

