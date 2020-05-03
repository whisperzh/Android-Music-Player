package com.example.learnintent.BroadcastReceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PowerManager;

/**
 * @ 莫若吻
 * @description home键监测、系统锁屏状态监
 */
public class BroadcastReceiverListener {
    private Context mContext;
    private ScreenBroadcastReceiver screenReceiver;
    private ScreenStateListener screenStateListener;

    /*
     * Home 键
     */
    private static final String SYSTEM_DIALOG_REASON_KEY = "reason";
    private static final String SYSTEM_DIALOG_REASON_RECENT_APPS = "recentapps";
    private static final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";
    private static final String SYSTEM_DIALOG_REASON_LOCK = "lock";
    private static final String SYSTEM_DIALOG_REASON_ASSIST = "assist";

    public BroadcastReceiverListener(Context context) {
        mContext = context;
        screenReceiver = new ScreenBroadcastReceiver();
    }
    /**
     * 广播接收者
     */
    private class ScreenBroadcastReceiver extends BroadcastReceiver {
        private String action = null;
        @Override
        public void onReceive(Context context, Intent intent) {
            action = intent.getAction();
            switch (action){
                case Intent.ACTION_SCREEN_ON: // 开屏
                    screenStateListener.onScreenOn();
                    break;
                case Intent.ACTION_SCREEN_OFF:// 锁屏
                    screenStateListener.onScreenOff();
                    break;
                case Intent.ACTION_USER_PRESENT:// 解锁
                    screenStateListener.onUserPresent();
                    break;
                case Intent.ACTION_CLOSE_SYSTEM_DIALOGS://home键监测
                    /*
                     * 这里监听了手机系统按下home键的那一刻事件，
                     * 如果想再处理再次回到app应用的事物，请参考home键及应用重新启动的过程生命周期，根据实际需求进行相关的操作的。
                     */
                    setHomeListener(intent);
                    break;
                default:
                    break;
            }
        }
    }
    private void setHomeListener(Intent intent) {
        String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
        switch (reason){//以下判断多个条件是为了不同安卓手机系统做适配
            case SYSTEM_DIALOG_REASON_HOME_KEY: // 短按Home键
            case SYSTEM_DIALOG_REASON_RECENT_APPS:  // 长按Home键 或者 activity切换键
            case SYSTEM_DIALOG_REASON_LOCK: // 锁屏
            case SYSTEM_DIALOG_REASON_ASSIST: // samsung 长按Home键
                screenStateListener.onHome();
                break;
            default:
                break;
        }
    }
    /**
     * 开始监听screen状态
     * @param listener
     */
    public void start(ScreenStateListener listener) {
        screenStateListener = listener;
        registerBroadcastReceiver();
        getScreenState();
    }
    /**
     * 获取screen状态
     */
    private void getScreenState() {
        PowerManager manager = (PowerManager) mContext
                .getSystemService(Context.POWER_SERVICE);
        if (manager.isScreenOn()) {
            if (screenStateListener != null) {
                screenStateListener.onScreenOn();
            }
        } else {
            if (screenStateListener != null) {
                screenStateListener.onScreenOff();
            }
        }
    }
    /**
     * 停止监听，销毁广播
     */
    public void unregisterBroadcastReceiver() {
        mContext.unregisterReceiver(screenReceiver);
    }
    /**
     * 启动screen状态和home键的广播接收器
     */
    private void registerBroadcastReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        filter.addAction(Intent.ACTION_USER_PRESENT);
        filter.addAction(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        mContext.registerReceiver(screenReceiver, filter);
    }
    public interface ScreenStateListener {// 返回给调用者屏幕状态信息
        public void onScreenOn();
        public void onScreenOff();
        public void onUserPresent();
        public void onHome();
    }
}