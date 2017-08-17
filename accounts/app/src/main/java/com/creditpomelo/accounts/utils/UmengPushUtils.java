package com.creditpomelo.accounts.utils;

import android.app.Notification;
import android.content.Context;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.android.common.utils.AppManager;
import com.android.common.utils.AppUtils;
import com.android.common.utils.LogUtils;
import com.creditpomelo.accounts.BuildConfig;
import com.creditpomelo.accounts.R;
import com.creditpomelo.accounts.main.main.event.MainEvent;
import com.umeng.message.IUmengCallback;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;

import org.greenrobot.eventbus.EventBus;

/**
 * 友盟推送
 * Created by fengyulong on 2016/8/19.
 */
public class UmengPushUtils {

    /**
     * 测试模式
     */
    public static void init(Context context) {
        PushAgent.getInstance(context).setDebugMode(BuildConfig.LOG_DEBUG);
        PushAgent.getInstance(context).register(new IUmengRegisterCallback() {

            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回device token
                LogUtils.i("UMessage diviceToken : " + deviceToken);
            }

            @Override
            public void onFailure(String s, String s1) {

            }
        });

        setMessageHandler(context);
    }

    /**
     * 开启推送
     */
    public static void enable(Context context) {
        PushAgent.getInstance(context).enable(new IUmengCallback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure(String s, String s1) {

            }
        });
    }

    /**
     * 关闭推送
     */
    public static void disable(Context context) {
        PushAgent.getInstance(context).disable(new IUmengCallback() {
            @Override
            public void onSuccess() {

            }

            @Override
            public void onFailure(String s, String s1) {

            }
        });
    }

    /**
     * 统计应用启动
     */
    public static void onAppStart(Context context) {
        PushAgent.getInstance(context).onAppStart();
    }

    /**
     * 设置用户id
     *
     * @param alias
     * @return
     */
    public static void addAlias(Context context, String alias) {
        addAlias(context, alias, "accounts", new UTrack.ICallBack() {
            @Override
            public void onMessage(boolean b, String s) {
                LogUtils.i("addAlias result " + b + ", message " + s);
            }
        });
    }

    /**
     * 设置用户id
     * 设置用户id和device_token的一对多的映射关系
     */
    public static void addAlias(Context context, String alias, String type, UTrack.ICallBack iCallBack) {
        try {
            PushAgent.getInstance(context).addAlias(alias, type, iCallBack);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置用户唯一id
     * 设置用户id和device_token的一一映射关系，确保同一个alias只对应一台设备
     */
    public static void addExclusiveAlias(Context context, String alias, String type, UTrack.ICallBack iCallBack) {
        try {
            PushAgent.getInstance(context).addExclusiveAlias(alias, type, iCallBack);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除用户id
     */
    public static void removeAlias(Context context, String alias) {
        removeAlias(context, alias, "accounts", new UTrack.ICallBack() {
            @Override
            public void onMessage(boolean b, String s) {
                LogUtils.i("removeAlias result " + b + ", message " + s);
            }
        });
    }

    /**
     * 删除用户id
     */
    public static void removeAlias(Context context, String alias, String type, UTrack.ICallBack iCallBack) {
        try {
            PushAgent.getInstance(context).removeAlias(alias, type, iCallBack);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 自定义消息处理类
     */
    private static void setMessageHandler(Context context) {
        PushAgent.getInstance(context).setMessageHandler(new UmengMessageHandler() {
            /**
             * 参考集成文档的1.6.3
             * http://dev.umeng.com/push/android/integration#1_6_3
             * */
            @Override
            public void dealWithCustomMessage(final Context context, final UMessage msg) {
                new Handler().post(new Runnable() {

                    @Override
                    public void run() {
                        // 对自定义消息的处理方式，点击或者忽略
//                        boolean isClickOrDismissed = true;
//                        if (isClickOrDismissed) {
                        //自定义消息的点击统计
                        UTrack.getInstance(context).trackMsgClick(msg);
//                        } else {
//                            //自定义消息的忽略统计
//                            UTrack.getInstance(context).trackMsgDismissed(msg);
//                        }

                        //启动UDPService
//                        if (!WokeUtil.isServiceRunning(context, WokeConstants.UDP_SERVICES_NAME)) {
//                            Intent service = new Intent(context, MessageUdpService.class);
//                            context.startService(service);
//                        }

                        //收消息,ReceiveMessageServices
//                        EventBus.getDefault().post(new ReceiveMessageEvent(ReceiveMessageEvent.REFRESH_MESSAGE));
                    }
                });
            }

            /**
             * 参考集成文档的1.6.4
             * http://dev.umeng.com/push/android/integration#1_6_4
             * */
            @Override
            public Notification getNotification(Context context,
                                                UMessage msg) {
                SharedPrefsUtils.putIsNewMessage(true);
                EventBus.getDefault().post(new MainEvent(MainEvent.Events.NEW_MESSAGE));
                switch (msg.builder_id) {
                    case 1:
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                        RemoteViews myNotificationView = new RemoteViews(context.getPackageName(), R.layout.notification_view);
                        myNotificationView.setTextViewText(R.id.notification_title, msg.title);
                        myNotificationView.setTextViewText(R.id.notification_text, msg.text);
                        myNotificationView.setImageViewBitmap(R.id.notification_large_icon, getLargeIcon(context, msg));
                        myNotificationView.setImageViewResource(R.id.notification_small_icon, getSmallIconId(context, msg));
                        builder.setContent(myNotificationView)
                                .setSmallIcon(getSmallIconId(context, msg))
                                .setTicker(msg.ticker)
                                .setAutoCancel(true);

                        return builder.build();

                    default:
                        //默认为0，若填写的builder_id并不存在，也使用默认。
                        return super.getNotification(context, msg);
                }
            }
        });
        PushAgent.getInstance(context).setNotificationClickHandler(new UmengNotificationClickHandler() {
            @Override
            public void launchApp(Context context, UMessage uMessage) {
                if (AppUtils.isAppOnForeground(context)) {
                    AppManager.getAppManager().finishAllActivity();
                } else {
                    super.launchApp(context, uMessage);
                }
            }

            @Override
            public void openUrl(Context context, UMessage uMessage) {
                super.openUrl(context, uMessage);
            }

            @Override
            public void openActivity(Context context, UMessage uMessage) {
                super.openActivity(context, uMessage);
            }

            @Override
            public void dealWithCustomAction(Context context, UMessage msg) {
                super.dealWithCustomAction(context, msg);
//                Toast.makeText(context, msg.custom, Toast.LENGTH_LONG).show();
            }
        });
    }

}
