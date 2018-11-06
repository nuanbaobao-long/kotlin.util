package com.rfid.handler.usehandler.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.camera2.CameraManager;
import android.os.Build;

public class FlashlightUtil {

    private CameraManager mCameraManager;
    private Camera mCamera;
    private boolean lightState = false;

    public boolean openFlashLight(Activity activity, Camera camera) {
        mCamera = camera;
        initCameraManager(activity);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                mCameraManager.setTorchMode("0", !lightState);
                lightState = !lightState;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            if (lightState) {
                if (mCamera != null) {
                    mCamera.stopPreview();
                    mCamera.release();
                    mCamera = null;
                }
            } else {
                final PackageManager pm = activity.getPackageManager();
                final FeatureInfo[] features = pm.getSystemAvailableFeatures();
                for (final FeatureInfo f : features) {
                    if (PackageManager.FEATURE_CAMERA_FLASH.equals(f.name)) { // 判断设备是否支持闪光灯
                        if (null == mCamera) {
                            mCamera = Camera.open();
                        }
                        final Camera.Parameters parameters = mCamera.getParameters();
                        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
                        mCamera.setParameters(parameters);
                        mCamera.startPreview();
                    }
                }
            }
            lightState = !lightState;
        }
        return lightState;
    }

    private CameraManager initCameraManager(Activity activity) {
        if (mCameraManager == null)
            mCameraManager = (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);
        return mCameraManager;
    }
}