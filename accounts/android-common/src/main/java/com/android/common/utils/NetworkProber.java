/**
 *
 */
package com.android.common.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import com.android.common.app.BaseApp;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class NetworkProber {

    public static final String CTWAP = "ctwap";
    public static final String CTNET = "ctnet";
    public static final String CMWAP = "cmwap";
    public static final String CMNET = "cmnet";
    public static final String NET_3G = "3gnet";
    public static final String WAP_3G = "3gwap";
    public static final String UNIWAP = "uniwap";
    public static final String UNINET = "uninet";

    public static final int TYPE_CT_WAP = 5;
    public static final int TYPE_CT_NET = 6;
    public static final int TYPE_CT_WAP_2G = 7;
    public static final int TYPE_CT_NET_2G = 8;

    public static final int TYPE_CM_WAP = 9;
    public static final int TYPE_CM_NET = 10;
    public static final int TYPE_CM_WAP_2G = 11;
    public static final int TYPE_CM_NET_2G = 12;

    public static final int TYPE_CU_WAP = 13;
    public static final int TYPE_CU_NET = 14;
    public static final int TYPE_CU_WAP_2G = 15;
    public static final int TYPE_CU_NET_2G = 16;

    public static final int TYPE_OTHER = 17;
    /**
     * 没有网络
     */
    public static final int TYPE_NET_WORK_DISABLED = 0;

    /**
     * wifi网络
     */
    public static final int TYPE_WIFI = 4;
    public static Uri PREFERRED_APN_URI = Uri.parse("content://telephony/carriers/preferapn");

    private NetworkProber() {
    }

    /***
     * 判断Network具体类型（联通移动wap，电信wap，其他net）
     */
    public static int checkNetworkType(Context mContext) {
        try {
            final ConnectivityManager connectivityManager = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
            final NetworkInfo mobNetInfoActivity = connectivityManager.getActiveNetworkInfo();
            if (mobNetInfoActivity == null || !mobNetInfoActivity.isAvailable()) {
                // 注意一：
                // NetworkInfo 为空或者不可以用的时候正常情况应该是当前没有可用网络，
                // 但是有些电信机器，仍可以正常联网，
                // 所以当成net网络处理依然尝试连接网络。
                // （然后在socket中捕捉异常，进行二次判断与用户提示）。
                return TYPE_NET_WORK_DISABLED;
            } else {
                // NetworkInfo不为null开始判断是网络类型
                int netType = mobNetInfoActivity.getType();
                if (netType == ConnectivityManager.TYPE_WIFI) {
                    // wifi net处理
                    return TYPE_WIFI;
                } else if (netType == ConnectivityManager.TYPE_MOBILE) {
                    // 注意二：
                    // 判断是否电信wap:
                    // 不要通过getExtraInfo获取接入点名称来判断类型，
                    // 因为通过目前电信多种机型测试发现接入点名称大都为#777或者null，
                    // 电信机器wap接入点中要比移动联通wap接入点多设置一个用户名和密码,
                    // 所以可以通过这个进行判断！

                    boolean is3G = isFastMobileNetwork(mContext);

                    final Cursor c = mContext.getContentResolver().query(PREFERRED_APN_URI, null, null, null, null);
                    if (c != null) {
                        c.moveToFirst();
                        final String user = c.getString(c.getColumnIndex("user"));
                        if (!TextUtils.isEmpty(user)) {
                            if (user.startsWith(CTWAP)) {
                                return is3G ? TYPE_CT_WAP : TYPE_CT_WAP_2G;
                            } else if (user.startsWith(CTNET)) {
                                return is3G ? TYPE_CT_NET : TYPE_CT_NET_2G;
                            }
                        }
                    }
                    c.close();

                    // 注意三：
                    // 判断是移动联通wap:
                    // 其实还有一种方法通过getString(c.getColumnIndex("proxy")获取代理ip
                    // 来判断接入点，10.0.0.172就是移动联通wap，10.0.0.200就是电信wap，但在
                    // 实际开发中并不是所有机器都能获取到接入点代理信息，例如魅族M9 （2.2）等...
                    // 所以采用getExtraInfo获取接入点名字进行判断

                    String netMode = mobNetInfoActivity.getExtraInfo();
                    Log.i("", "==================netmode:" + netMode);
                    if (netMode != null) {
                        // 通过apn名称判断是否是联通和移动wap
                        netMode = netMode.toLowerCase();

                        if (netMode.equals(CMWAP)) {
                            return is3G ? TYPE_CM_WAP : TYPE_CM_WAP_2G;
                        } else if (netMode.equals(CMNET)) {
                            return is3G ? TYPE_CM_NET : TYPE_CM_NET_2G;
                        } else if (netMode.equals(NET_3G) || netMode.equals(UNINET)) {
                            return is3G ? TYPE_CU_NET : TYPE_CU_NET_2G;
                        } else if (netMode.equals(WAP_3G) || netMode.equals(UNIWAP)) {
                            return is3G ? TYPE_CU_WAP : TYPE_CU_WAP_2G;
                        }
                    }
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return TYPE_OTHER;
        }

        return TYPE_OTHER;

    }

    /***
     * 判断Network具体类型（联通移动wap，电信wap，其他net）
     */
    private static boolean isFastMobileNetwork(Context context) {
        TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        switch (telephonyManager.getNetworkType()) {
            case TelephonyManager.NETWORK_TYPE_1xRTT:
                return false; // ~ 50-100 kbps
            case TelephonyManager.NETWORK_TYPE_CDMA:
                return false; // ~ 14-64 kbps
            case TelephonyManager.NETWORK_TYPE_EDGE:
                return false; // ~ 50-100 kbps
            case TelephonyManager.NETWORK_TYPE_EVDO_0:
                return true; // ~ 400-1000 kbps
            case TelephonyManager.NETWORK_TYPE_EVDO_A:
                return true; // ~ 600-1400 kbps
            case TelephonyManager.NETWORK_TYPE_GPRS:
                return false; // ~ 100 kbps
            case TelephonyManager.NETWORK_TYPE_HSDPA:
                return true; // ~ 2-14 Mbps
            case TelephonyManager.NETWORK_TYPE_HSPA:
                return true; // ~ 700-1700 kbps
            case TelephonyManager.NETWORK_TYPE_HSUPA:
                return true; // ~ 1-23 Mbps
            case TelephonyManager.NETWORK_TYPE_UMTS:
                return true; // ~ 400-7000 kbps
            case TelephonyManager.NETWORK_TYPE_EHRPD:
                return true; // ~ 1-2 Mbps
            case TelephonyManager.NETWORK_TYPE_EVDO_B:
                return true; // ~ 5 Mbps
            case TelephonyManager.NETWORK_TYPE_HSPAP:
                return true; // ~ 10-20 Mbps
            case TelephonyManager.NETWORK_TYPE_IDEN:
                return false; // ~25 kbps
            case TelephonyManager.NETWORK_TYPE_LTE:
                return true; // ~ 10+ Mbps
            case TelephonyManager.NETWORK_TYPE_UNKNOWN:
                return false;
            default:
                return false;
        }
    }

    /***
     * 判断Network具体类型（联通移动wap，电信wap，其他net）
     */
    public static String checkNetType(Context context) {
        int checkNetworkType = checkNetworkType(context);
        String networktype = "";
        switch (checkNetworkType) {
            case TYPE_WIFI:
                networktype = "wifi";
                break;
            case TYPE_NET_WORK_DISABLED:
                networktype = "nonetwork";
                break;
            case TYPE_CT_WAP:
                networktype = "ctwap";
                break;
            case TYPE_CT_WAP_2G:
                networktype = "ctwap_2g";
                break;
            case TYPE_CT_NET:
                networktype = "ctnet";
                break;
            case TYPE_CT_NET_2G:
                networktype = "ctnet_2g";
                break;
            case TYPE_CM_WAP:
                networktype = "cmwap";
                break;
            case TYPE_CM_WAP_2G:
                networktype = "cmwap_2g";
                break;
            case TYPE_CM_NET:
                networktype = "cmnet";
                break;
            case TYPE_CM_NET_2G:
                networktype = "cmnet_2g";
                break;
            case TYPE_CU_NET:
                networktype = "cunet";
                break;
            case TYPE_CU_NET_2G:
                networktype = "cunet_2g";
                break;
            case TYPE_CU_WAP:
                networktype = "cuwap";
                break;
            case TYPE_CU_WAP_2G:
                networktype = "cuwap_2g";
                break;
            case TYPE_OTHER:
                networktype = "other";
                break;
            default:
                break;
        }
        return networktype;
    }

    public static String getIpAddress() {
        int checkNetworkType = checkNetworkType(BaseApp.getInstance().getBaseContext());
        String ipAddress = "";
        if (TYPE_NET_WORK_DISABLED == checkNetworkType) {
            ipAddress = "";
        } else if (TYPE_WIFI == checkNetworkType) {
            ipAddress = getWifiIpAddress(BaseApp.getInstance().getBaseContext());
        } else {
            ipAddress = getLocalIpAddress();
        }
        return ipAddress;
    }

    private static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            LogUtils.e("WifiPreference IpAddress" + ex.toString());
        }
        return null;
    }

    private static String getWifiIpAddress(Context context) {
        // 获取wifi服务
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        // 判断wifi是否开启
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }
        return intToIp(wifiManager.getConnectionInfo().getIpAddress());
    }

    private static String intToIp(int i) {
        return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF) + "." + (i >> 24 & 0xFF);
    }

    /**
     * 判断网络连接
     *
     * @return true：连接网络（wif和3G） 。false：无网络连接
     */
    public static boolean getDeviceNetwork(Context context) {
        NetworkInfo.State state = null;
        ConnectivityManager connManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        // 获得网络类型
        state = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .getState();// wifi网络
        if (NetworkInfo.State.CONNECTED == state) {// 网络连接状态
            return true;
        }
        state = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                .getState();// gprs网络
        if (NetworkInfo.State.CONNECTED == state) {// 网络连接状态
            return true;
        } else {
            return false;
        }
    }
}
