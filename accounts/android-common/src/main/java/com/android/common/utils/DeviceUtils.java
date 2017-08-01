package com.android.common.utils;

import android.telephony.TelephonyManager;

import com.android.common.app.BaseApp;

/**
 * Created by fengyulong on 2016/7/25.
 */
public class DeviceUtils {
    private static DeviceUtils instance;
    private String deviceId;// 设备ID
    private String softwareVersion;// 设备软件版本号(不准)
    private String phoneNumber;// 手机号(不是都能获取)
    private String deviceModel;// 手机型号
    private String release;// android系统版本号
    private String service;//服务商

    private DeviceUtils() {
        init();
    }

    public static DeviceUtils getInstance() {
        if (instance == null) {
            synchronized (DeviceUtils.class) {
                if (instance == null) {
                    instance = new DeviceUtils();
                }
            }
        }
        return instance;
    }

    public void init() {
        try {
            deviceModel = android.os.Build.MODEL;// 手机型号
            service = "未知";
            release = android.os.Build.VERSION.RELEASE;// android系统版本号

            TelephonyManager tele = (TelephonyManager) BaseApp.getInstance().getSystemService(BaseApp.getInstance().TELEPHONY_SERVICE);

            /**
             * 唯一的设备ID：
             * GSM手机的 IMEI 和 CDMA手机的 MEID.
             * Return null if device ID is not available.
             */
            deviceId = tele.getDeviceId();// 设备ID

            softwareVersion = tele.getDeviceSoftwareVersion();// 设备软件版本号
            phoneNumber = tele.getLine1Number();// 手机号

            String android_imsi = tele.getSubscriberId();//
            if (android_imsi != null) {
                if (android_imsi.startsWith("46000") || android_imsi.startsWith("46002")) {
                    // 因为移动网络编号46000下的IMSI已经用完，所以虚拟了一个46002编号，134/159号段使用了此编号</span><span></span></span>
                    service = "中国移动";
                } else if (android_imsi.startsWith("46001")) {
                    service = "中国联通";
                } else if (android_imsi.startsWith("46003")) {
                    service = "中国电信";
                }
            } else {
                service = "未知";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getDeviceId() {
//        if (ActivityCompat.checkSelfPermission(BaseApp.getInstance(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
//            return;
//        }
        if (ValidateUtil.isEmpty(deviceId)) {
            init();
        }
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getSoftwareVersion() {
        return softwareVersion;
    }

    public void setSoftwareVersion(String softwareVersion) {
        this.softwareVersion = softwareVersion;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getRelease() {
        return release;
    }

    public void setRelease(String release) {
        this.release = release;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }
}
