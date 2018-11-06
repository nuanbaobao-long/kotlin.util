package com.rfid.handler.usehandler.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.ArraySet;
import android.widget.Button;
import android.widget.Toast;

import java.util.Arrays;
import java.util.Set;

public class PermissionUtil {
    public static final int PERMISSION_CODE = 10086;

    /**
     * 电话那一块的权限(不包括编辑联系人)
     */
    public static final int PERMISSION_CALL = 10001;
    /**
     * 相机权限
     */
    public static final int PERMISSION_CAMERA = 10002;
    /**
     * 日历的权限
     */
    public static final int PERMISSION_CALENDAR = 10003;
    /**
     * 联系人权限
     */
    public static final int PERMISSION_CONTACTS = 10004;
    /**
     * 定位权限
     */
    public static final int PERMISSION_LOCATION = 10005;
    /**
     * 录音权限
     */
    public static final int PERMISSION_RECORD = 10006;
    /**
     * 短信权限
     */
    public static final int PERMISSION_SMS = 10007;
    /**
     * 读写内存卡权限
     */
    public static final int PERMISSION_STORAGE = 10008;
    /**
     * 读写内存卡权限
     */
    public static final int PERMISSION_BODY_SENSORS = 10009;

    private static final String READ_CALENDAR = "android.permission.READ_CALENDAR";
    private static final String WRITE_CALENDAR = "android.permission.WRITE_CALENDAR";
    private static final String CAMERA_ = "android.permission.CAMERA";
    private static final String READ_CONTACTS = "android.permission.READ_CONTACTS";
    private static final String WRITE_CONTACTS = "android.permission.WRITE_CONTACTS";
    private static final String GET_ACCOUNTS = "android.permission.GET_ACCOUNTS";
    private static final String ACCESS_FINE_LOCATION = "android.permission.ACCESS_FINE_LOCATION";
    private static final String ACCESS_COARSE_LOCATION = "android.permission.ACCESS_COARSE_LOCATION";
    private static final String RECORD_AUDIO_ = "android.permission.RECORD_AUDIO";
    private static final String READ_PHONE_STATE = "android.permission.READ_PHONE_STATE";
    private static final String CALL_PHONE = "android.permission.CALL_PHONE";
    private static final String READ_CALL_LOG = "android.permission.READ_CALL_LOG";
    private static final String WRITE_CALL_LOG = "android.permission.WRITE_CALL_LOG";
    private static final String USE_SIP = "android.permission.USE_SIP";
    private static final String PROCESS_OUTGOING_CALLS = "android.permission.PROCESS_OUTGOING_CALLS";
    private static final String BODY_SENSORS_ = "android.permission.BODY_SENSORS";
    private static final String SEND_SMS = "android.permission.SEND_SMS";
    private static final String RECEIVE_SMS = "android.permission.RECEIVE_SMS";
    private static final String READ_SMS = "android.permission.READ_SMS";
    private static final String RECEIVE_WAP_PUSH = "android.permission.RECEIVE_WAP_PUSH";
    private static final String RECEIVE_MMS = "android.permission.RECEIVE_MMS";
    private static final String READ_EXTERNAL_STORAGE = "android.permission.READ_EXTERNAL_STORAGE";
    private static final String WRITE_EXTERNAL_STORAGE = "android.permission.WRITE_EXTERNAL_STORAGE";

    public static final String[] CALENDAR = new String[]{READ_CALENDAR, WRITE_CALENDAR};
    public static final String[] CAMERA = new String[]{CAMERA_};
    public static final String[] CONTACTS = new String[]{READ_CONTACTS, WRITE_CONTACTS, GET_ACCOUNTS};
    public static final String[] LOCATION = new String[]{ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION};
    public static final String[] RECORD_AUDIO = new String[]{RECORD_AUDIO_};
    public static final String[] CALL = new String[]{READ_PHONE_STATE, CALL_PHONE, READ_CALL_LOG, WRITE_CALL_LOG, USE_SIP, PROCESS_OUTGOING_CALLS};
    public static final String[] BODY_SENSORS = new String[]{BODY_SENSORS_};
    public static final String[] SMS = new String[]{SEND_SMS, RECEIVE_SMS, READ_SMS, RECEIVE_WAP_PUSH, RECEIVE_MMS};
    public static final String[] STORAGE = new String[]{READ_EXTERNAL_STORAGE, WRITE_EXTERNAL_STORAGE};


    public interface Callback {
        void resultState(boolean ok);
    }

    private static Callback callback_;

    public static void setCallback(Callback callback) {
        callback_ = callback;
    }


    /**
     * 判断是否拥有AndroidManifest中全部隐私权限,没有就一起申请了
     *
     * @param activity
     * @return 返回是否拥有全部私有权限
     */
    public static boolean RequestAllPermission(Activity activity) throws PackageManager.NameNotFoundException {
        return RequestAllPermission(activity, null);
    }

    /**
     * 判断是否拥有AndroidManifest中全部隐私权限,没有就一起申请了
     *
     * @param activity
     * @param callback 这个callback,我感觉没什么用啊,但是还是想写进去怎么办,这个的效果和return是一样的
     * @return 返回是否拥有全部私有权限
     */
    public static boolean RequestAllPermission(Activity activity, Callback callback) throws PackageManager.NameNotFoundException {
        boolean hasPermission = false;
        String[] permissions = NoPermission(activity,getAllPermissions(activity));
        if (permissions.length != 0) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permissions[0])) {
                showDialog(activity, permissions);
            } else
                ActivityCompat.requestPermissions(activity, permissions, PERMISSION_CODE);
            hasPermission = false;
        } else {
            hasPermission = true;
        }
        if (null != callback)
            callback.resultState(hasPermission);
        return hasPermission;
    }

    /**
     * 判断是否得到权限
     *
     * @param activity
     * @param callback
     * @param PermissionType
     * @return
     */
    public static boolean ResultPermission(Activity activity, Callback callback, int... PermissionType) {
        boolean hasPermission = HasPermission(activity, getPermissions(PermissionType));
        if (callback != null)
            callback.resultState(hasPermission);
        return hasPermission;
    }

    /**
     * 判断是否拥有这些权限,有就返回true,没有就返回false并全部申请
     *
     * @param activity
     * @param PermissionType
     * @return
     */
    public static boolean RequestPermission(Activity activity, int... PermissionType) {
        String[] permission = NoPermission(activity, getPermissions(PermissionType));
        if (permission.length != 0) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission[0])) {
                showDialog(activity, permission);
            } else
                ActivityCompat.requestPermissions(activity, permission, PERMISSION_CODE);
            return false;
        } else {
            return true;
        }
    }

    /**
     * 返回没有获得授权的权限数组
     *
     * @param activity
     * @param permissions
     * @return
     */
    private static String[] NoPermission(Activity activity, String[] permissions) {
        Set<String> newPermissions = new ArraySet<>();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (int i = 0; i < permissions.length; i++) {
                if (ContextCompat.checkSelfPermission(activity, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                    newPermissions.add(permissions[i]);
                }
            }
        }
        return newPermissions.toArray(new String[newPermissions.size()]);
    }

    /**
     * 判断是否有这个权限啦
     *
     * @param activity
     * @param PermissionType 这里的类型就调用PermissionUtil里面定义的值
     * @return
     */
    public static boolean HasPermission(Activity activity, int... PermissionType) {
        return ResultPermission(activity, null, PermissionType);
    }

    /**
     * 判断是否拥有了AndroidManifest中的全部隐私权限
     *
     * @param activity
     * @return
     */
    public static boolean HasAllPermission(Activity activity) throws PackageManager.NameNotFoundException {
        return HasPermission(activity, getAllPermissions(activity));
    }

    /**
     * 判断是否有这些权限了
     *
     * @param activity
     * @param permissions 权限列表咯
     * @return
     */
    public static boolean HasPermission(Activity activity, String[] permissions) {
        if (NoPermission(activity, permissions).length > 0)
            return false;
        else return true;
    }

    public static String[] getAllPermissions(Activity activity) throws PackageManager.NameNotFoundException {
        PackageManager packageManager = activity.getPackageManager();
        String packageName = activity.getPackageName();
        PackageInfo packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS);
        return packageInfo.requestedPermissions;
    }

    /**
     * 根据权限类型获取权限数组
     *
     * @param PermissionType
     * @return
     */
    private static String[] getPermissions(int... PermissionType) {
        Set<String> permissions = new ArraySet<>();
        for (int i = 0; i < PermissionType.length; i++) {
            int type = PermissionType[i];
            if (type == PERMISSION_CALENDAR)
                permissions.addAll(Arrays.asList(CALENDAR));
            else if (type == PERMISSION_CALL)
                permissions.addAll(Arrays.asList(CALL));
            else if (type == PERMISSION_CAMERA)
                permissions.addAll(Arrays.asList(CAMERA));
            else if (type == PERMISSION_CONTACTS)
                permissions.addAll(Arrays.asList(CONTACTS));
            else if (type == PERMISSION_LOCATION)
                permissions.addAll(Arrays.asList(LOCATION));
            else if (type == PERMISSION_RECORD)
                permissions.addAll(Arrays.asList(RECORD_AUDIO));
            else if (type == PERMISSION_SMS)
                permissions.addAll(Arrays.asList(SMS));
            else if (type == PERMISSION_STORAGE)
                permissions.addAll(Arrays.asList(STORAGE));
            else if (type == PERMISSION_BODY_SENSORS)
                permissions.addAll(Arrays.asList(BODY_SENSORS));
        }
        return permissions.toArray(new String[permissions.size()]);
    }

    /**
     * 根据需要申请的权限列表显示需要哪些权限
     *
     * @param activity
     * @param permission
     */
    private static void showDialog(final Activity activity, final String[] permission) {
        new AlertDialog.Builder(activity)
                .setTitle("权限申请")
                .setMessage("我们需要使用" + getPermissionName(permission) + "权限,如果您拒绝当前权限,将会导致部分功能无法使用")
                .setNegativeButton("授权", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ActivityCompat.requestPermissions(activity, permission, PERMISSION_CODE);
                    }
                })
                .setNeutralButton("拒绝", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(activity, "您已拒绝当前权限,无法使用部分功能", Toast.LENGTH_SHORT).show();
                    }
                }).create().show();
    }

    /**
     * 根据权限申请列表返回权限的名字
     *
     * @param permission
     * @return
     */
    public static String getPermissionName(String[] permission) {
        StringBuilder sb = new StringBuilder(" ");
        Set<String> permissionNames = new ArraySet<>();
        for (int i = 0; i < permission.length; i++) {
            if (permission[i].equals(READ_CALENDAR)) {
                permissionNames.add("日历");
            } else if (permission[i].equals(WRITE_CALENDAR)) {
                permissionNames.add("日历");
            } else if (permission[i].equals(CAMERA_)) {
                permissionNames.add("相机");
            } else if (permission[i].equals(READ_CONTACTS)) {
                permissionNames.add("读取联系人");
            } else if (permission[i].equals(WRITE_CONTACTS)) {
                permissionNames.add("添加联系人");
            } else if (permission[i].equals(GET_ACCOUNTS)) {
                permissionNames.add("获取账户信息");
            } else if (permission[i].equals(ACCESS_FINE_LOCATION)) {
                permissionNames.add("定位");
            } else if (permission[i].equals(ACCESS_COARSE_LOCATION)) {
                permissionNames.add("定位");
            } else if (permission[i].equals(RECORD_AUDIO_)) {
                permissionNames.add("麦克风");
            } else if (permission[i].equals(READ_PHONE_STATE)) {
                permissionNames.add("电话");
            } else if (permission[i].equals(CALL_PHONE)) {
                permissionNames.add("电话");
            } else if (permission[i].equals(READ_CALL_LOG)) {
                permissionNames.add("电话");
            } else if (permission[i].equals(WRITE_CALL_LOG)) {
                permissionNames.add("电话");
            } else if (permission[i].equals(USE_SIP)) {
                permissionNames.add("电话");
            } else if (permission[i].equals(PROCESS_OUTGOING_CALLS)) {
                permissionNames.add("电话");
            } else if (permission[i].equals(BODY_SENSORS_)) {
                permissionNames.add("传感器");
            } else if (permission[i].equals(SEND_SMS)) {
                permissionNames.add("短信");
            } else if (permission[i].equals(RECEIVE_SMS)) {
                permissionNames.add("短信");
            } else if (permission[i].equals(READ_SMS)) {
                permissionNames.add("短信");
            } else if (permission[i].equals(RECEIVE_WAP_PUSH)) {
                permissionNames.add("短信");
            } else if (permission[i].equals(RECEIVE_MMS)) {
                permissionNames.add("短信");
            } else if (permission[i].equals(READ_EXTERNAL_STORAGE)) {
                permissionNames.add("存储");
            } else if (permission[i].equals(WRITE_EXTERNAL_STORAGE)) {
                permissionNames.add("存储");
            }
        }
        String[] permissionName = permissionNames.toArray(new String[permissionNames.size()]);
        for (int j = 0; j < permissionName.length; j++) {
            sb.append(permissionName[j]);
            if (j != permissionName.length - 1)
                sb.append(",");
        }
        sb.append("等");
        return sb.toString();
    }
}
