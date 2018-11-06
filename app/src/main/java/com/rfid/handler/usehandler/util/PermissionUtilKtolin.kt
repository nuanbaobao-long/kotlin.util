package com.rfid.handler.usehandler.util

import android.app.Activity
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.util.ArraySet
import android.widget.Toast

import java.util.Arrays

/**
 * 判断是否拥有AndroidManifest中全部隐私权限,没有就一起申请了
 *
 * @param activity
 * @return 返回是否拥有全部私有权限
 */
class PermissionUtilKtolin {

    companion object {
        @JvmStatic
        val PERMISSION_CODE = 10086

        /**
         * 电话那一块的权限(不包括编辑联系人)
         */
        @JvmStatic
        val PERMISSION_CALL = 10001
        /**
         * 相机权限
         */
        @JvmStatic
        val PERMISSION_CAMERA = 10002
        /**
         * 日历的权限
         */
        @JvmStatic
        val PERMISSION_CALENDAR = 10003
        /**
         * 联系人权限
         */
        @JvmStatic
        val PERMISSION_CONTACTS = 10004
        /**
         * 定位权限
         */
        @JvmStatic
        val PERMISSION_LOCATION = 10005
        /**
         * 录音权限
         */
        @JvmStatic
        val PERMISSION_RECORD = 10006
        /**
         * 短信权限
         */
        @JvmStatic
        val PERMISSION_SMS = 10007
        /**
         * 读写内存卡权限
         */
        @JvmStatic
        val PERMISSION_STORAGE = 10008

        private val READ_CALENDAR = "android.permission.READ_CALENDAR"
        private val WRITE_CALENDAR = "android.permission.WRITE_CALENDAR"
        private val CAMERA_ = "android.permission.CAMERA"
        private val READ_CONTACTS = "android.permission.READ_CONTACTS"
        private val WRITE_CONTACTS = "android.permission.WRITE_CONTACTS"
        private val GET_ACCOUNTS = "android.permission.GET_ACCOUNTS"
        private val ACCESS_FINE_LOCATION = "android.permission.ACCESS_FINE_LOCATION"
        private val ACCESS_COARSE_LOCATION = "android.permission.ACCESS_COARSE_LOCATION"
        private val RECORD_AUDIO_ = "android.permission.RECORD_AUDIO"
        private val READ_PHONE_STATE = "android.permission.READ_PHONE_STATE"
        private val CALL_PHONE = "android.permission.CALL_PHONE"
        private val READ_CALL_LOG = "android.permission.READ_CALL_LOG"
        private val WRITE_CALL_LOG = "android.permission.WRITE_CALL_LOG"
        private val USE_SIP = "android.permission.USE_SIP"
        private val PROCESS_OUTGOING_CALLS = "android.permission.PROCESS_OUTGOING_CALLS"
        private val BODY_SENSORS_ = "android.permission.BODY_SENSORS"
        private val SEND_SMS = "android.permission.SEND_SMS"
        private val RECEIVE_SMS = "android.permission.RECEIVE_SMS"
        private val READ_SMS = "android.permission.READ_SMS"
        private val RECEIVE_WAP_PUSH = "android.permission.RECEIVE_WAP_PUSH"
        private val RECEIVE_MMS = "android.permission.RECEIVE_MMS"
        private val READ_EXTERNAL_STORAGE = "android.permission.READ_EXTERNAL_STORAGE"
        private val WRITE_EXTERNAL_STORAGE = "android.permission.WRITE_EXTERNAL_STORAGE"

        val CALENDAR = arrayOf(READ_CALENDAR, WRITE_CALENDAR)
        val CAMERA = arrayOf(CAMERA_)
        val CONTACTS = arrayOf(READ_CONTACTS, WRITE_CONTACTS, GET_ACCOUNTS)
        val LOCATION = arrayOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION)
        val RECORD_AUDIO = arrayOf(RECORD_AUDIO_)
        val CALL = arrayOf(READ_PHONE_STATE, CALL_PHONE, READ_CALL_LOG, WRITE_CALL_LOG, USE_SIP, PROCESS_OUTGOING_CALLS)
        val BODY_SENSORS = arrayOf(BODY_SENSORS_)
        val SMS = arrayOf(SEND_SMS, RECEIVE_SMS, READ_SMS, RECEIVE_WAP_PUSH, RECEIVE_MMS)
        val STORAGE = arrayOf(READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE)

        private var callback_: Callback? = null


        interface Callback {
            fun resultState(ok: Boolean)
        }

        fun setCallback(callback: Callback) {
            callback_ = callback
        }

        /**
         * 判断是否拥有AndroidManifest中全部隐私权限,没有就一起申请了
         *
         * @param activity
         * @param callback 这个callback,我感觉没什么用啊,但是还是想写进去怎么办,这个的效果和return是一样的
         * @return 返回是否拥有全部私有权限
         */
        @Throws(PackageManager.NameNotFoundException::class)
        @JvmOverloads
        @JvmStatic
        fun RequestAllPermission(activity: Activity, callback: Callback? = null): Boolean {
            var hasPermission = false
            val permissions = NoPermission(activity, getAllPermissions(activity))
            if (permissions.size != 0) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permissions[0])) {
                    showDialog(activity, permissions)
                } else
                    ActivityCompat.requestPermissions(activity, permissions, PERMISSION_CODE)
                hasPermission = false
            } else {
                hasPermission = true
            }
            callback?.resultState(hasPermission)
            return hasPermission
        }

        /**
         * 判断是否得到权限
         *
         * @param activity
         * @param callback
         * @param PermissionType
         * @return
         */
        @JvmStatic
        fun ResultPermission(activity: Activity, callback: Callback?, vararg PermissionType: Int): Boolean {
            val hasPermission = HasPermission(activity, getPermissions(*PermissionType))
            callback?.resultState(hasPermission)
            return hasPermission
        }

        /**
         * 判断是否拥有这些权限,有就返回true,没有就返回false并全部申请
         *
         * @param activity
         * @param PermissionType
         * @return
         */
        @JvmStatic
        fun RequestPermission(activity: Activity, vararg PermissionType: Int): Boolean {
            val permission = NoPermission(activity, getPermissions(*PermissionType))
            if (permission.size != 0) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission[0])) {
                    showDialog(activity, permission)
                } else
                    ActivityCompat.requestPermissions(activity, permission, PERMISSION_CODE)
                return false
            } else {
                return true
            }
        }

        /**
         * 返回没有获得授权的权限数组
         *
         * @param activity
         * @param permissions
         * @return
         */
        private fun NoPermission(activity: Activity, permissions: Array<String>): Array<String> {
            val newPermissions = ArraySet<String>()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                for (i in permissions.indices) {
                    if (ContextCompat.checkSelfPermission(activity, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                        newPermissions.add(permissions[i])
                    }
                }
            }
            return newPermissions.toTypedArray()
        }

        /**
         * 判断是否有这个权限啦
         *
         * @param activity
         * @param PermissionType 这里的类型就调用PermissionUtil里面定义的值
         * @return
         */
        @JvmStatic
        fun HasPermission(activity: Activity, vararg PermissionType: Int): Boolean {
            return ResultPermission(activity, null, *PermissionType)
        }

        /**
         * 判断是否拥有了AndroidManifest中的全部隐私权限
         *
         * @param activity
         * @return
         */
        @Throws(PackageManager.NameNotFoundException::class)
        @JvmStatic
        fun HasAllPermission(activity: Activity): Boolean {
            return HasPermission(activity, getAllPermissions(activity))
        }

        /**
         * 判断是否有这些权限了
         *
         * @param activity
         * @param permissions 权限列表咯
         * @return
         */
        @JvmStatic
        fun HasPermission(activity: Activity, permissions: Array<String>): Boolean {
            return if (NoPermission(activity, permissions).size > 0)
                false
            else
                true
        }

        @Throws(PackageManager.NameNotFoundException::class)
        @JvmStatic
        fun getAllPermissions(activity: Activity): Array<String> {
            val packageManager = activity.packageManager
            val packageName = activity.packageName
            val packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS)
            return packageInfo.requestedPermissions
        }

        /**
         * 根据权限类型获取权限数组
         *
         * @param PermissionType
         * @return
         */
        private fun getPermissions(vararg PermissionType: Int): Array<String> {
            val permissions = ArraySet<String>()
            for (i in PermissionType.indices) {
                val type = PermissionType[i]
                if (type == PERMISSION_CALENDAR)
                    permissions.addAll(Arrays.asList(*CALENDAR))
                else if (type == PERMISSION_CALL)
                    permissions.addAll(Arrays.asList(*CALL))
                else if (type == PERMISSION_CAMERA)
                    permissions.addAll(Arrays.asList(*CAMERA))
                else if (type == PERMISSION_CONTACTS)
                    permissions.addAll(Arrays.asList(*CONTACTS))
                else if (type == PERMISSION_LOCATION)
                    permissions.addAll(Arrays.asList(*LOCATION))
                else if (type == PERMISSION_RECORD)
                    permissions.addAll(Arrays.asList(*RECORD_AUDIO))
                else if (type == PERMISSION_SMS)
                    permissions.addAll(Arrays.asList(*SMS))
                else if (type == PERMISSION_STORAGE)
                    permissions.addAll(Arrays.asList(*STORAGE))
            }
            return permissions.toTypedArray()
        }

        /**
         * 根据需要申请的权限列表显示需要哪些权限
         *
         * @param activity
         * @param permission
         */
        private fun showDialog(activity: Activity, permission: Array<String>) {
            AlertDialog.Builder(activity)
                    .setTitle("权限申请")
                    .setMessage("我们需要使用" + getPermissionName(permission) + "权限,如果您拒绝当前权限,将会导致部分功能无法使用")
                    .setNegativeButton("授权") { dialog, which -> ActivityCompat.requestPermissions(activity, permission, PERMISSION_CODE) }
                    .setNeutralButton("拒绝") { dialog, which -> Toast.makeText(activity, "您已拒绝当前权限,无法使用部分功能", Toast.LENGTH_SHORT).show() }.create().show()
        }

        /**
         * 根据权限申请列表返回权限的名字
         *
         * @param permission
         * @return
         */
        @JvmStatic
        fun getPermissionName(permission: Array<String>): String {
            val sb = StringBuilder(" ")
            val permissionNames = ArraySet<String>()
            for (i in permission.indices) {
                if (permission[i] == READ_CALENDAR) {
                    permissionNames.add("日历")
                } else if (permission[i] == WRITE_CALENDAR) {
                    permissionNames.add("日历")
                } else if (permission[i] == CAMERA_) {
                    permissionNames.add("相机")
                } else if (permission[i] == READ_CONTACTS) {
                    permissionNames.add("读取联系人")
                } else if (permission[i] == WRITE_CONTACTS) {
                    permissionNames.add("添加联系人")
                } else if (permission[i] == GET_ACCOUNTS) {
                    permissionNames.add("获取账户信息")
                } else if (permission[i] == ACCESS_FINE_LOCATION) {
                    permissionNames.add("定位")
                } else if (permission[i] == ACCESS_COARSE_LOCATION) {
                    permissionNames.add("定位")
                } else if (permission[i] == RECORD_AUDIO_) {
                    permissionNames.add("麦克风")
                } else if (permission[i] == READ_PHONE_STATE) {
                    permissionNames.add("电话")
                } else if (permission[i] == CALL_PHONE) {
                    permissionNames.add("电话")
                } else if (permission[i] == READ_CALL_LOG) {
                    permissionNames.add("电话")
                } else if (permission[i] == WRITE_CALL_LOG) {
                    permissionNames.add("电话")
                } else if (permission[i] == USE_SIP) {
                    permissionNames.add("电话")
                } else if (permission[i] == PROCESS_OUTGOING_CALLS) {
                    permissionNames.add("电话")
                } else if (permission[i] == BODY_SENSORS_) {
                    permissionNames.add("传感器")
                } else if (permission[i] == SEND_SMS) {
                    permissionNames.add("短信")
                } else if (permission[i] == RECEIVE_SMS) {
                    permissionNames.add("短信")
                } else if (permission[i] == READ_SMS) {
                    permissionNames.add("短信")
                } else if (permission[i] == RECEIVE_WAP_PUSH) {
                    permissionNames.add("短信")
                } else if (permission[i] == RECEIVE_MMS) {
                    permissionNames.add("短信")
                } else if (permission[i] == READ_EXTERNAL_STORAGE) {
                    permissionNames.add("存储")
                } else if (permission[i] == WRITE_EXTERNAL_STORAGE) {
                    permissionNames.add("存储")
                }
            }
            val permissionName = permissionNames.toTypedArray()
            for (j in permissionName.indices) {
                sb.append(permissionName[j])
                if (j != permissionName.size - 1)
                    sb.append(",")
            }
            sb.append("等")
            return sb.toString()
        }
    }
}
