package com.hujinwen.kuaishou;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.view.ViewGroup;

import com.yxcorp.gifshow.recycler.widget.CustomRecyclerView;
import com.yxcorp.plugin.search.SearchActivity;

import gz.com.alibaba.fastjson.JSONArray;
import gz.com.alibaba.fastjson.JSONObject;
import gz.radar.Android;
import gz.radar.AndroidUI;
import k.a.b.o.y;

/**
 * Created by hu-jinwen on 2020/9/8
 */
@SuppressWarnings("ResourceType")
public class SearchFeed {


    /**
     * 搜索视频
     */
    public static void searchFeed(final String keyword) throws Exception {
        Activity topActivity = Android.getTopActivity();
        Intent a2 = k.i.a.a.a.a(topActivity, SearchActivity.class, "searchKeyword", keyword);
        a2.putExtra("searchSource", y.SEARCH_BANNED_HOT_QUERY);
        topActivity.startActivity(a2);

        Thread.sleep(5000);

        android.view.View viewById = AndroidUI.findViewById(2131306210);
        android.view.View view = AndroidUI.findViewByText(viewById, "视频");
        if (view != null) {
            do {
                if (view.isClickable()) {
                    final android.view.View clickView = view;
                    view.post(new Runnable() {
                        public void run() {
                            clickView.performClick();
                        }
                    });
                    Thread.sleep(2000);
                    break;
                }
                view = (android.view.View) view.getParent();
            } while (view != null);
        } else {
            throw new RuntimeException("Cannot find '直播' view!");
        }

        Thread.sleep(3000);

        android.view.View viewById1 = AndroidUI.findViewById(2131307768);
        java.util.List<android.view.View> views = AndroidUI.findViewsById(viewById1, 2131304946);

        final CustomRecyclerView view1 = (CustomRecyclerView) views.get(4);

        for (int i = 0; i < 10; i++) {
            view1.post(new Runnable() {
                @Override
                public void run() {
                    try {
                        view1.smoothScrollToPosition(view1.getAdapter().getItemCount() - 1);
                    } catch (Exception ignored) {
                    }
                }
            });
            Thread.sleep(2000);
        }
    }


    private static String viewTree(android.view.View view) throws Exception {
        Resources var1 = Android.getApplication().getResources();
        return viewTreeScan(view, 0, var1).toString();
    }

    private static JSONObject viewTreeScan(android.view.View var0, int var1, Resources var2) throws Exception {
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
        if (var0 instanceof android.widget.TextView) {
            var3.put("ViewText", ((android.widget.TextView) var0).getText().toString());
        } else if (var0 instanceof ViewGroup) {
            JSONArray var11 = new JSONArray();
            ViewGroup var5 = (ViewGroup) var0;
            int var6 = var1 + 1;

            for (int var7 = 0; var7 < var5.getChildCount(); ++var7) {
                android.view.View var8 = var5.getChildAt(var7);
                JSONObject var9 = viewTreeScan(var8, var6, var2);
                var11.add(var9);
            }

            var3.put("ChildViews", var11);
        }

        return var3;
    }


}
