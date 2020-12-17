package com.hujinwen.tiktok;

import android.app.Activity;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bytedance.ies.dmt.ui.widget.DmtStatusView;
import com.bytedance.ies.dmt.ui.widget.DmtTextView;
import com.bytedance.router.SmartRouter;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.ss.android.ugc.aweme.search.activity.SearchResultActivity;

import java.util.List;

import gz.com.alibaba.fastjson.JSONArray;
import gz.com.alibaba.fastjson.JSONObject;
import gz.radar.Android;
import gz.radar.AndroidUI;

/**
 * 抖音直播
 * apk版本：12.2.0
 */
@SuppressWarnings("ResourceType")
public class TiktokLive {

    public static void searchLive(final String keyword) throws Exception {
        try {
            SmartRouter.buildRoute(Android.getTopActivity(), "//search?keyword=" + keyword + "&display_keyword=" + keyword + "&enter_from=anywheredoor").open();

            Thread.sleep(3000);
            Activity activity = Android.getTopActivity();
            View viewById = activity.findViewById(2131174479);

            View view = AndroidUI.findViewByText(viewById, "直播");
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
            Thread.sleep(3000);
            slide();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private static void slide() throws Exception {
        StringBuilder sb = new StringBuilder();
        try {
            Activity activity = Android.getTopActivity();
            if (activity instanceof SearchResultActivity) {
                activity = Android.getTopActivity();

                List<View> viewsById = AndroidUI.findViewsById(activity.getWindow().getDecorView(), 2131170444);

                final RecyclerView view = (RecyclerView) viewsById.get(1);

                for (int i = 0; i < 500; i++) {
                    // 判断是否结束了
                    if (isEnd(view)) {
                        Thread.sleep(3000);
                        if (isEnd(view)) {
                            break;
                        }
                    }

                    view.post(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                view.smoothScrollToPosition(view.getAdapter().getItemCount() - 1);
                            } catch (Exception ignored) {
                            }
                        }
                    });
                    Thread.sleep(2000);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     * 抓取结束
     */
    private static boolean isEnd(RecyclerView bb2) {
        View lastChildView = bb2.getChildAt(bb2.getChildCount() - 1);
        if (!(lastChildView instanceof DmtStatusView)) {
            return false;
        }
        DmtStatusView lastChild = (DmtStatusView) lastChildView;
        if (lastChild.getChildCount() == 3) {
            View view = lastChild.getChildAt(1);
            if (view instanceof DmtTextView) {
                DmtTextView textView = (DmtTextView) view;
                return textView.getText().toString().contains("暂时没有更多了");
            }
        }
        return false;
    }

    private static String viewTree(View view) throws Exception {
        Resources var1 = Android.getApplication().getResources();
        return viewTreeScan(view, 0, var1).toString();
    }

    private static JSONObject viewTreeScan(View var0, int var1, Resources var2) throws Exception {
        JSONObject var3 = new JSONObject();
        var3.put("ViewClass", var0.getClass().getName());
        var3.put("ViewId", var0.getId());

        try {
            String var4 = var2.getResourceEntryName(var0.getId());
            var3.put("ViewIdName", var4);
        } catch (Resources.NotFoundException var10) {
        }

        var3.put("IsClickable", var0.isClickable());
        var3.put("IsVisible", var0.getVisibility() == 0);
        var3.put("IsEnabled", var0.isEnabled());
        var3.put("IsFocusable", var0.isFocusable());
        var3.put("IsFocused", var0.isFocused());
        var3.put("IsHorizontalScrollBarEnabled", var0.isHorizontalScrollBarEnabled());
        var3.put("IsLongClickable", var0.isLongClickable());
        var3.put("IsSelected", var0.isSelected());
        var3.put("IsShown", var0.isShown());
        var3.put("Width", var0.getWidth());
        var3.put("Height", var0.getHeight());
        var3.put("X", var0.getX());
        var3.put("Y", var0.getY());
        var3.put("ViewDeep", var1);
        if (var0 instanceof TextView) {
            var3.put("ViewText", ((TextView) var0).getText().toString());
        } else if (var0 instanceof ViewGroup) {
            JSONArray var11 = new JSONArray();
            ViewGroup var5 = (ViewGroup) var0;
            int var6 = var1 + 1;

            for (int var7 = 0; var7 < var5.getChildCount(); ++var7) {
                View var8 = var5.getChildAt(var7);
                JSONObject var9 = viewTreeScan(var8, var6, var2);
                var11.add(var9);
            }

            var3.put("ChildViews", var11);
        }

        return var3;
    }

    public static void main(String[] args) {
        Gson gson = new Gson();
        JsonElement jsonElement = gson.toJsonTree(new Object());
        JsonObject a = jsonElement.getAsJsonObject().get("a").getAsJsonObject();
        JsonObject d = a.get("d").getAsJsonObject();
        JsonObject b = d.get("b").getAsJsonObject();
        JsonObject a1 = b.get("a").getAsJsonObject();
        JsonObject a2 = a1.get("a").getAsJsonObject();


    }


}
