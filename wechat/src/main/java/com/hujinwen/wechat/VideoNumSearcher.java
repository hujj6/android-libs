package com.hujinwen.wechat;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.tencent.mm.plugin.finder.feed.ui.FinderProfileUI;
import com.tencent.mm.plugin.finder.g.a;
import com.tencent.mm.plugin.finder.model.BaseFinderFeed;
import com.tencent.mm.plugin.finder.search.FinderContactSearchUI;
import com.tencent.mm.plugin.finder.search.FinderFeedSearchUI;
import com.tencent.mm.protocal.protobuf.FinderObject;
import com.tencent.mm.protocal.protobuf.ahf;
import com.tencent.mm.ui.LauncherUI;

import java.util.ArrayList;
import java.util.List;

import gz.radar.Android;
import gz.radar.AndroidUI;
import gz.util.X;

/**
 * Created by hu-jinwen on 2020/10/15
 * <p>
 * 微信视频号搜索
 * apk版本：7.0.12
 */
@SuppressWarnings("ResourceType")
public class VideoNumSearcher {

    private static final Instrumentation inst = new Instrumentation();


    /**
     * 搜索视频号
     *
     * @param keyword 搜索用的关键字
     * @return 搜索到的所有结果
     */
    public static List<FinderObject> searchVideoNum(final String keyword) throws Exception {
        Activity activity = Android.getTopActivity();
        if (!(activity instanceof FinderFeedSearchUI)) {
            AndroidUI.topActivityStartActivity(FinderFeedSearchUI.class.getName());
            Thread.sleep(1000);
            activity = Android.getTopActivity();
        }
        if (activity instanceof FinderFeedSearchUI) {
            final FinderFeedSearchUI finderFeedSearchUI = (FinderFeedSearchUI) activity;
            finderFeedSearchUI.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        X.invokeObject(finderFeedSearchUI, "UX", new Class[]{String.class}, new Object[]{keyword});
                    } catch (Exception ignored) {
                    }
                }
            });
            Thread.sleep(1000 * 5);
            TextView loadMoreTextView = (TextView) AndroidUI.findViewByIdName("cya");
            int lastSize = getFinderObjects(finderFeedSearchUI).size();
            final RecyclerView recyclerView = finderFeedSearchUI.findViewById(2131303628);
            for (int i = 0; i < 10; i++) {
                recyclerView.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            recyclerView.smoothScrollToPosition(getFinderObjects(finderFeedSearchUI).size() - 1);
                        } catch (Exception ignored) {
                        }
                    }
                });
                Thread.sleep(2000);
                AndroidUI.hover(200, 300, 10);
                Thread.sleep(1000 * 5);
                int currentSize = getFinderObjects(finderFeedSearchUI).size();
                int continueFlag = X.getField(finderFeedSearchUI, "continueFlag");
                if (continueFlag != 1 || loadMoreTextView.getText().toString().contains("没有")) {
                    break;
                } else if (lastSize == currentSize) {
                    break;
                }
                lastSize = currentSize;
            }
            return getFinderObjects(finderFeedSearchUI);
        }
        return null;
    }

    /**
     * 搜索视频号用户
     */
    public static void searchVideoNumUser(final String keyword) throws Exception {
        // 退出当前activity
        Activity currentActivity;
        while (!((currentActivity = Android.getTopActivity()) instanceof LauncherUI)) {
            currentActivity.finish();
            Thread.sleep(1000);
        }
        // 打开搜索，并输入
        Intent intent = new Intent();
        intent.putExtra("request_type", 1);
        ahf ahf = new ahf();
        ahf.query = keyword;
        a.a(Android.getTopActivity(), ahf, intent);

        Thread.sleep(1000);
        for (int i = 0; i < 5; i++) {
            // 连续的模拟按下回车键，直到出现了结果，重复5次
            inst.sendKeyDownUpSync(66);
            Thread.sleep(1500);
            // 判断页面是否加载
            if (AndroidUI.findViewById(2131300827) != null) {
                break;
            }

        }
    }

    /**
     * 获取 FinderContactSearchUI 中封装的所有，contact 对象。
     */
    public static String getUIContacts() throws Exception {
        Activity topActivity = Android.getTopActivity();
        if (topActivity instanceof FinderContactSearchUI) {
            FinderContactSearchUI finderContactSearchUI = (FinderContactSearchUI) topActivity;
            return gz.com.alibaba.fastjson.JSON.toJSONString(X.getField(finderContactSearchUI, "pFH"));
        }
        return "None";
    }

    /**
     * 根据用户id，打开用户详情页面
     */
    public static void openProfileById(String userId) throws Exception {
        // 退出当前activity
        Activity currentActivity;
        while (!((currentActivity = Android.getTopActivity()) instanceof LauncherUI)) {
            currentActivity.finish();
            Thread.sleep(1000);
        }
        // 开始搜索
        Intent intent = new Intent();
        intent.putExtra("finder_username", userId);
        com.tencent.mm.plugin.finder.g.a.enterFinderProfileUI(Android.getTopActivity(), intent);
        Thread.sleep(1500);
        // 滑动所有的页数
        Activity activity = Android.getTopActivity();
        if (activity instanceof FinderProfileUI) {
            final RecyclerView bb2 = activity.findViewById(2131303209);
            for (int i = 0; i < 500; i++) {
                // 判断是否结束了
                if (isEnd(2131301290)) {
                    Thread.sleep(3000);
                    if (isEnd(2131301290)) {
                        break;
                    }
                }
                bb2.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            bb2.smoothScrollToPosition(bb2.getAdapter().getItemCount() - 1);
                        } catch (Exception ignored) {
                        }
                    }
                });
                AndroidUI.hover(300, 400, 100);
                Thread.sleep(2000);
            }
        }
    }

    /**
     * 获取UI中包含的数据对象
     *
     * @return UI中包含的数据
     */
    private static List<FinderObject> getFinderObjects(FinderFeedSearchUI finderFeedSearchUI) throws Exception {
        List<FinderObject> finderObjects = new ArrayList<>();
        ArrayList<BaseFinderFeed> baseFinderFeeds = X.getField(finderFeedSearchUI, "pus");
        for (BaseFinderFeed baseFinderFeed : baseFinderFeeds) {
            FinderObject finderObject = X.getField(baseFinderFeed.feedObject, "ppF");
            if (finderObject != null) {
                finderObjects.add(finderObject);
            }
        }
        return finderObjects;
    }

    private static boolean isEnd(int id) throws Exception {
        View view = AndroidUI.findViewById(id);

        if (!(view instanceof TextView)) {
            return false;
        }
        TextView textView = (TextView) view;
        CharSequence text = textView.getText();
        return String.valueOf(text).contains("没有更多了");
    }


}
