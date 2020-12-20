package com.hujinwen.xhs;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.hujinwen.tools.AndroidUI;
import com.hujinwen.tools.DebuggerTool;
import com.xingin.shield.http.XhsHttpInterceptor;
import com.xingin.xhs.index.v2.IndexActivityV2;

import java.io.IOException;

import l.f0.e1.a.a;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by hu-jinwen on 12/1/20
 * <p>
 * 小红书搜索
 * app 版本：v.6.70.0.a4851e2
 */
@SuppressLint("ResourceType")
public class Searcher {

    private static Response response;

    private static final OkHttpClient OK_HTTP_CLIENT;

    private static final Gson GSON = new Gson();

    static {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        clientBuilder.addInterceptor(ShieldInterceptor.newInstance("myXhsHttpInterceptor"));
        OK_HTTP_CLIENT = clientBuilder.build();
    }


    /**
     * 搜索全部
     *
     * @param keyword:   关键字
     * @param orderBy:   排序方式 0 -> 综合，1 -> 最热，2 -> 最新
     * @param pageLimit: 限制最大页数，0 为不限，
     */
    public static void searchAll(final String keyword, int orderBy, int pageLimit) throws Exception {
        // 打开搜索界面
        AndroidUI.startActivity("com.xingin.alioth.search.GlobalSearchActivity");
        Thread.sleep(1000);
        // 输入关键字
        DebuggerTool.sendKeywordById(2131299808, keyword);
        Thread.sleep(500);
        // 点击搜索
        DebuggerTool.clickById(2131299812);
        Thread.sleep(2000);
        // 选择排序
        if (orderBy == 1) {
            DebuggerTool.forceClickById(2131301584);
        } else if (orderBy == 2) {
            DebuggerTool.forceClickById(2131301585);
        }
        Thread.sleep(2000);
        // 滚动抓取
        View viewById = AndroidUI.findViewById(2131299795);
        if (!(viewById instanceof RecyclerView)) {
            return;
        }
        Thread.sleep(1000);
        final RecyclerView recyclerView = (RecyclerView) viewById;
        if (pageLimit == 0) {
            pageLimit = 100000000;
        }
        for (int i = 0; i < pageLimit; i++) {
            if (isEnd()) {
                break;
            }
            recyclerView.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
                    } catch (Exception ignored) {
                    }
                }
            });
            Thread.sleep(1500);
        }
        Thread.sleep(2000);
        // 退出activity
        Activity topActivity;
        while (!(topActivity = AndroidUI.getTopActivity()).getClass().getName().contains("IndexActivityV2")) {
            topActivity.finish();
            Thread.sleep(1000);
        }
    }

    /**
     * 判断列表是否抓取结束
     */
    private static boolean isEnd() throws Exception {
        View endView = AndroidUI.findViewById(2131297978);
        if (!(endView instanceof AppCompatTextView)) {
            return false;
        }
        AppCompatTextView textView = (AppCompatTextView) endView;
        return textView.getText().toString().contains("THE END");
    }

    /**
     * 通过用户发布的作品id，打开用户主页
     *
     * @param feedId:         作品ID
     * @param targetActivity: activity name
     *                        com.xingin.matrix.v2.notedetail.NoteDetailActivity = 笔记（图文）
     *                        com.xingin.matrix.videofeed.ui.VideoFeedActivityV2 = 视频
     */
    public static void openProfileByFeedId(String feedId, String targetActivity) throws Exception {
        // 打开作品详情
        openNote(feedId, targetActivity);
        Thread.sleep(3000);
        // 打开用户主页
        int userId = targetActivity.contains("NoteDetailActivity") ? 2131300305 : 2131299937;
        DebuggerTool.clickById(userId);
    }


    /**
     * 打开作品
     *
     * @param feedId:         作品ID
     * @param targetActivity: activity name
     *                        com.xingin.matrix.v2.notedetail.NoteDetailActivity = 笔记（图文）
     *                        com.xingin.matrix.videofeed.ui.VideoFeedActivityV2 = 视频
     */
    public static void openNote(String feedId, String targetActivity) throws Exception {
        Activity topActivity = AndroidUI.getTopActivity();
        Class<?> aClass = Class.forName(targetActivity);

        Bundle bundle1 = new Bundle();
        bundle1.putString("id", feedId);

        Intent intent = new Intent(topActivity, aClass);
        intent.putExtras(bundle1);
        l.f0.u1.l0.b.a(topActivity, intent, 666);
    }

    /**
     * 退回主页面，防止不必要的进程内存占用过高
     */
    public static void backToHome() throws Exception {
        Activity topActivity;
        while (!((topActivity = AndroidUI.getTopActivity()) instanceof IndexActivityV2)) {
            topActivity.finish();
            Thread.sleep(1000);
        }
    }

    /**
     * 生成小红书请求所需的 shield 等请求头信息
     */
    public static String genShield(String url, String xy_common_params) throws IOException {
        response = null;

        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.addHeader("xy-common-params", xy_common_params);
        requestBuilder.addHeader("User-Agent", "Dalvik/2.1.0 (Linux; U; Android 9; Pixel 2 Build/PQ2A.190405.003) Resolution/1080*1920 Version/6.70.0 Build/6700132 Device/(Google;Pixel 2) discover/6.70.0 NetType/WiFi");

        Request request = requestBuilder.url(url).get().build();
        try {
            OK_HTTP_CLIENT.newCall(request).execute();
        } catch (Exception ignored) {
        }
        if (response != null) {
//            return JsonUtils.toString(response.request().headers());
            return GSON.toJson(response.request().headers());
        }

        return "{}";
    }

    static class ShieldInterceptor extends XhsHttpInterceptor {

        public ShieldInterceptor(String s, a<Request> a) {
            super(s, a);
        }

        public static ShieldInterceptor newInstance(String var0) {
            return new ShieldInterceptor(var0, null);
        }

        @Override
        public Response intercept(Chain chain) throws IOException {
            Response response = super.intercept(chain);
            if (response != null) {
                Searcher.response = response;
                throw new IOException("");
            }
            return null;
        }
    }

}
