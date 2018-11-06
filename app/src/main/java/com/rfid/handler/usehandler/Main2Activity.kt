package com.rfid.handler.usehandler

import android.view.View
import com.rfid.handler.usehandler.bean.Schoole
import com.rfid.handler.usehandler.bean.Student
import com.rfid.handler.usehandler.bean.StudentModule
import com.rfid.handler.usehandler.interfaces.DaggerStudentInfomation
import com.rfid.handler.usehandler.util.showDialog
import com.rfid.park.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main2.*
import javax.inject.Inject
import javax.inject.Named

/**
 * auther yhl
 * data:20181030
 * 学习dagger2
 */

class Main2Activity : BaseActivity() {
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tv_m2_test_inject -> {
                tv_m2_test_show.text = "student infomation:\nstudent name:${student.name}\nstudent.sex:${student.sex}" +
                        "\nstudent.age:${student.age}\nstudent.schoole.name:${student.schoole.schooleName}\n" +
                        "student.schoole.address:${student.schoole.schooleAddress}"
            }
            R.id.tv_m2_test_module -> {
                "登录成功了".showDialog(this@Main2Activity, {
                    "点击了确定".showToast()
                }, {
                    "点击了取消".showToast()
                },"红豆泥","无所得斯")
            }
            R.id.tv_m2_test_zhuru -> {
                "登陆成攻".showDialog(this@Main2Activity,{
                    "只有一个确定".showToast()
                },titleText = "修改一哈标题咯")
            }
            R.id.tv_m2_test_test -> {
            }
        }
    }

    override fun getContentView(): Int {
        return R.layout.activity_main2
    }

    @Inject
    lateinit var schoole: Schoole
    @Inject
    lateinit var student: Student
    @Inject
    @Named("schooleName")
    lateinit var schooleName: String
    @Inject
    @Named("schooleAddress")
    lateinit var schooleAddress: String

    override fun initView() {
        schoole = Schoole()
        schooleName = "重庆电子工程学院"
        schooleAddress = "重大A区"
        schoole.schooleAddress = schooleAddress
        schoole.schooleName = schooleName
        var dagger = DaggerStudentInfomation.builder().
//                schooleMoudle(SchooleMoudle(schoole)).
                studentModule(StudentModule("张三", 12, "女", schoole)).build()
        schoole.schooleName = "中国世界星宇园"
//        dagger.addSchool(schoole)
        student = dagger.Infomation()
    }

}
