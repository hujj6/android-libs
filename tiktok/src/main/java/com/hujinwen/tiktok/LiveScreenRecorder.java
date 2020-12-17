package com.hujinwen.tiktok;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import com.ss.android.ugc.aweme.live.LivePlayActivity;
import com.ss.android.ugc.aweme.main.MainActivity;

import gz.radar.Android;
import gz.radar.AndroidUI;

/**
 * Created by hu-jinwen on 2020/10/9
 * 抖音直播录屏
 * apk版本：12.6.0
 */
public class LiveScreenRecorder {

    /**
     * 根据房间id，打开指定房间
     */
    public static void openLive(String roomId) throws Exception {
        LivePlayActivity.a(Android.getTopActivity(), Long.parseLong(roomId), new Bundle());
    }

    /**
     * 打开小时榜
     */
    public static void openHourTop() throws Exception {
        clickById(2131170886);
        Thread.sleep(10 * 1000);
        clickById(2131167252);
        Thread.sleep(3 * 1000);
    }

    /**
     * 返回主activity
     */
    public static void backToHomeActivity() throws Exception {
        Activity currentActivity;
        while (!((currentActivity = Android.getTopActivity()) instanceof MainActivity)) {
            currentActivity.finish();
            Thread.sleep(1000);
        }
    }

    /**
     * 通过id点击
     */
    private static void clickById(int id) throws Exception {
        View view = AndroidUI.findViewById(id);
        if (view != null) {
            do {
                if (view.isClickable()) {
                    final View clickView = view;
                    view.post(new Runnable() {
                        public void run() {
                            clickView.performClick();
                        }
                    });
                    Thread.sleep(2000);
                    break;
                }
                view = (View) view.getParent();
            } while (view != null);
        }
    }

}
