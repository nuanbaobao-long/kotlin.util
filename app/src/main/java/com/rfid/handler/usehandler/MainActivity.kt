package com.rfid.handler.usehandler

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Toast
import com.rfid.handler.usehandler.bean.MessageBean
import com.rfid.handler.usehandler.fragment.BlankFragment
import com.rfid.handler.usehandler.fragment.ItemFragment
import com.rfid.handler.usehandler.fragment.PlusOneFragment
import com.rfid.handler.usehandler.fragment.dummy.DummyContent
import com.rfid.handler.usehandler.util.MySharedPreference
import com.rfid.handler.usehandler.util.PermissionUtil
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class MainActivity : AppCompatActivity(), ViewPager.OnPageChangeListener, BlankFragment.OnFragmentInteractionListener, PlusOneFragment.OnFragmentInteractionListener, ItemFragment.OnListFragmentInteractionListener {

    override fun onFragmentInteraction(uri: Uri) {
    }

    override fun onListFragmentInteraction(item: DummyContent.DummyItem?) {
    }

    override fun onPageScrollStateChanged(p0: Int) {
    }

    override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
    }

    override fun onPageSelected(index: Int) {
        when (index) {
            0 -> navigation.selectedItemId = R.id.navigation_home
            1 -> navigation.selectedItemId = R.id.navigation_dashboard
            2 -> navigation.selectedItemId = R.id.navigation_notifications
        }
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                vpg_main.setCurrentItem(0)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                vpg_main.setCurrentItem(1)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                vpg_main.setCurrentItem(2)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    var fragmetns = ArrayList<Fragment>()

    companion object {
        var eventBus: EventBus? = null
    }

    var username :String by MySharedPreference(this,"username","")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)
        initView()
//        EventBus.getDefault().register(this)
        eventBus = EventBus()
        eventBus?.register(this)
        "username = $username".showToast()
        username = "黑老三"
        username.showToast()
    }

    //    @Subscribe(threadMode = ThreadMode.MAIN)
//    fun update(msg: String) {
//        vpg_main.setCurrentItem(0)
//    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun updat2e(d: Float) {
        vpg_main.setCurrentItem(0)
    }

    private fun String.showToast(){
        Toast.makeText(this@MainActivity,this,Toast.LENGTH_SHORT).show()
    }

    var num = 0
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        num++
        showToast(num.toString() +"次")
    }

    override fun onStop() {
        super.onStop()
        if (isFinishing)
            EventBus.getDefault().unregister(this)
    }

    var handler: Handler? = null
    private fun initView() {
        PermissionUtil.RequestAllPermission(this)
        fragmetns.add(PlusOneFragment.newInstance())
        fragmetns.add(ItemFragment.newInstance(1))
        fragmetns.add(BlankFragment.newInstance())
        vpg_main.adapter = object : FragmentPagerAdapter(this.supportFragmentManager) {
            override fun getItem(index: Int): Fragment {
                return fragmetns[index]
            }

            override fun getCount(): Int {
                return fragmetns.size
            }

        }
        vpg_main.setOnPageChangeListener(this@MainActivity)
    }

    var toast: Toast? = null
    fun showToast(msg: Any) {
        var m = ""
        if (msg is String) {
            m = msg
        } else if (msg is Int) {
            m = resources.getString(msg)
        }
        if (null == toast) {
            toast = Toast.makeText(this, m, Toast.LENGTH_SHORT)
            toast?.show()
        } else {
            toast?.setText(m)
            toast?.show()
        }

    }


}
