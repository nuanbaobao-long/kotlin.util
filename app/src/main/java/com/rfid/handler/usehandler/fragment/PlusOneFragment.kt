package com.rfid.handler.usehandler.fragment

import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.view.View
import com.rfid.handler.usehandler.Main2Activity

import com.rfid.handler.usehandler.R
import com.rfid.handler.usehandler.interfaces.TestInterfaceA
import com.rfid.handler.usehandler.interfaces.TestInterfaceB
import com.rfid.handler.usehandler.bean.MessageBean
import com.rfid.handler.usehandler.util.*
import kotlinx.android.synthetic.main.fragment_plus_one.*
import kotlin.properties.Delegates

class PlusOneFragment : BaseFragment(), View.OnClickListener, TestInterfaceA, TestInterfaceB {
    //这下面是两个接口都定义了同样的方法,然后可以根据super<Interface>来分别调用想调用的方法
    override fun test1() {
        super<TestInterfaceA>.test1()
        super<TestInterfaceB>.test1()
    }

    override fun test2() {
    }

    internal var PlusOneFragmentName = "testInternal"
    var messageBean = MessageBean(1, "test")
    override fun initView() {
        val (id, text) = messageBean.copy(id=12)//数据类与解构声明,这种定义必须放在方法里面,copy方法必须对应date数据
        //这段代码意思是将对象MessageBean复制一份并将对象中的id改为12在赋值给前面定义的两个值,下面就能直接用了
        "id=$id and text=$text".l()

    }

    inline fun <T> Int.add(i: T, comms: (t: T) -> Int): Int {
        return (this.plus(comms(i)))
    }

    override fun getContentView(): Int {
        return R.layout.fragment_plus_one
    }

    var porp: String
        get() = this::porp.name
        set(value) = value.showToast()
    var pp1 = "嘿嘿嘿"

    lateinit var phone: String
    var username: String by MySharedPreference(context, "username", "")
    /** by lazy 延迟委托,不会随着类的初始化而初始化,只会在第一次使用这个参数的时候才初始化这个值,*/
    val lazyName: String by lazy {
        "first use".l()
        "lazyName"
    }

    /** Delegates.observable 监听变化委托,在这个值有变化的时候,会走这个方法*/
    var observableString: String by Delegates.observable("oldValue") { prop, old, new ->
        //prop:当前参数的一些信息,比如这儿的参数名称
        " ${prop.name} + from $old update to $new".l()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_click -> {
//                porp = "lalalalal"
//                porp.showToast()//答应的porp参数名:porp
//                this::pp1.name.showToast()//打印的pp1的参数名:pp1

                lazyName.l()
                lazyName.l()//这两句最后打印出来的是:first use   lazyName    lazyName

                observableString = "newValue"//这句会打印 observableString from oldValue update to newValue
                observableString = "thereValue"//这两句会打印 observableString from newValue update to thereValue

                "username = $username".showToast()
                username = "黑老三"
                username.showToast()
                var names = "123"
                var booleans = true
                startActivity<Main2Activity>(names, booleans, booleans)
            }
            //kotlin封装了很多的高阶函数,比较方便使用,在kotlin.collections._Collections里面
            R.id.tv_test1 -> {//通过高阶函数实现重新创建list
                val items = listOf(MessageBean(1, "text1"), MessageBean(2, "test2"), MessageBean(3, "test3"), MessageBean(4, "test4"), MessageBean(5, "test5"))
                val item3 = items.addText("+1") { item: MessageBean, text: String ->
                    MessageBean(item.id, item.text + text)//如果是有返回值的lambda表达式,最后一行就是返回值,如果没有,那就不是
                }
                showData(item3)
            }
            R.id.tv_test2 -> {//通过高阶函数实现修改list里面的值
                val items = listOf(MessageBean(1, "text1"), MessageBean(2, "test2"), MessageBean(3, "test3"), MessageBean(4, "test4"), MessageBean(5, "test5"))
                items.addText2("+2") {//如果是有返回值的lambda表达式,最后一行就是返回值,如果没有,那就不是
                    item: MessageBean, text: String ->
                    item.text = item.text + text
                }
                showData(items)
            }
            R.id.tv_test3 -> {//通过高阶函数修改list里面的值,可以多个lambda一起传递
                val items = listOf(MessageBean(1, "text1"), MessageBean(2, "test2"), MessageBean(3, "test3"), MessageBean(4, "test4"), MessageBean(5, "test5"))
                items.addText3("+3", 1, {//如果是有返回值的lambda表达式,最后一行就是返回值,如果没有,那就不是,如果没有返回值,lambda也需要显示返回Unit
                    item: MessageBean, text: String ->
                    item.text += text
                }) {//最后一个lambda表达式可以写在括号外面
                    item: MessageBean, i: Int ->
                    item.id += i
                }
                showData(items)
            }
            R.id.tv_test4 -> {//通过高阶函数修改list里面的值,lambda里面可以不传值,具体查看addText4函数
                val items = listOf(MessageBean(1, "text1"), MessageBean(2, "test2"), MessageBean(3, "test3"), MessageBean(4, "test4"), MessageBean(5, "test5"))
                items.addText4 { "+4" }
                for (bean in items)
                    ("text:${bean.text} id:${bean.id}").l()
            }
            R.id.tv_test5 -> {
                val items = listOf(MessageBean(1, "text1"), MessageBean(2, "test2"), MessageBean(3, "test3"), MessageBean(4, "test4"), MessageBean(5, "test5"))
                items.addText5(5) { i: Int ->
                    this.id += i
                    this
                }
                showData(items)
            }
            R.id.tv_test6 -> {
                val items = listOf(MessageBean(1, "text1"), MessageBean(2, "test2"), MessageBean(3, "test3"), MessageBean(4, "test4"), MessageBean(5, "test5"))
                items.addText6(6, "+6") {
                    print("==========${this.id}")
                    this.id += it//如果lambda的参数只有一个的话,可以直接使用it,使用A.(B)这种方式,在这边可以为:A=this,B=it
                    this.text += "--"
                    {
                        { i: Int, s: String ->
                            this.text += s + "+6"
                            this.id += i + 10
                            {
                                print("${this@addText6.text}  ${this@addText6.id}")
                                this@addText6
                            }
                        }
                    }
                }
                showData(items)
            }
            R.id.tv_test7 -> {//匿名函数使用
                var i = fun(x: Int, y: Int): Int = x + y
                i(1, 3).toString().showToast()
            }
            R.id.tv_test8 -> {
                Handler().run { "内联函数调用,加上crossinline使用,具体介绍需要查看文章" }
            }
            R.id.tv_test9 -> {
                loop@ for (i in 0..10) {//这里命名了一个名为loop的标签,其实名字随意,只要添加了@就行
                    for (i in 10..20) {
                        i.toString().l()
                        //break//这里的break表示退出当前循环
                        break@loop//这里表示退出有loop标签的循环
                    }
                    i.toString().l()
                }
            }
            R.id.tv_test10 -> {
                listOf(1, 2, 3, 4, 5, 6, 7).forEach s@{
                    //名为s的标签,标签名随意,需要加@
                    //if (it == 3) return//这里的return和java一样,直接不会走后面的全部代码了,打印:12
                    if (it == 3) return@s //这里加了标签就不一样了,只不走局部代码,比如这儿的,就不走当前forEach的这一次循环了,打印124567
                    it.toString().l()
                }
                listOf(1, 2, 3, 4, 5, 6, 7).forEach {
                    //名为s的标签,标签名随意,需要加@
                    //if (it == 3) return//这里的return和java一样,直接不会走后面的全部代码了,打印:12
                    if (it == 3) return@forEach //这里加了标签就不一样了,只不走局部代码,比如这儿的,就不走当前forEach的这一次循环了,打印124567
                    it.toString().l()
                }
                "all of to run".l()
            }
        }
    }

    private fun showData(items: List<MessageBean>) {
        for (bean in items)
            ("text:${bean.text} id:${bean.id}").l()
        items.size.toString().showToast()
    }

    private var mListener: OnFragmentInteractionListener? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btn_click.setOnClickListener(this)
        tv_test1.setOnClickListener(this)
        tv_test2.setOnClickListener(this)
        tv_test3.setOnClickListener(this)
        tv_test4.setOnClickListener(this)
        tv_test5.setOnClickListener(this)
        tv_test6.setOnClickListener(this)
        tv_test7.setOnClickListener(this)
        tv_test8.setOnClickListener(this)
        tv_test9.setOnClickListener(this)
        tv_test10.setOnClickListener(this)
    }

    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        fun newInstance(): PlusOneFragment {
            val fragment = PlusOneFragment()
            val args = Bundle()
            fragment.arguments = args
            return fragment
        }
    }
}

