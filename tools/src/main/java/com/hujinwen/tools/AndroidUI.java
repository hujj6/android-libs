package com.hujinwen.tools;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.content.res.Resources;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hujinwen.utils.JsonUtils;
import com.hujinwen.utils.ReflectUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * Created by hu-jinwen on 12/16/20
 */
@SuppressWarnings("unchecked")
public class AndroidUI {

    public static void startActivity(String activityName) throws Exception {
        topActivityStartActivity(activityName);
    }

    public static void topActivityStartActivity(String activityName) throws Exception {
        Activity activity = getTopActivity();
        if (activity.getClass().getName().equals(activityName)) {
            return;
        }
        Class<? extends Activity> activityClass = (Class<? extends Activity>) Class.forName(activityName);
        Intent intent = new Intent(activity, activityClass);
        activity.startActivity(intent);
    }

    public static <T extends Activity> T getTopActivity() throws Exception {
        @SuppressLint("PrivateApi") Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
        Object activityThread = activityThreadClass.getMethod("currentActivityThread").invoke(null);
        Field activitiesField = activityThreadClass.getDeclaredField("mActivities");
        activitiesField.setAccessible(true);
        Map<Object, Object> activities = (Map<Object, Object>) activitiesField.get(activityThread);
        for (Object activityClientRecord : activities.values()) {
            Class<?> activityClietnRecordClass = activityClientRecord.getClass();
            Field pausedField = activityClietnRecordClass.getDeclaredField("paused");
            pausedField.setAccessible(true);
            if (!pausedField.getBoolean(activityClientRecord)) {
                Field activityField = activityClietnRecordClass.getDeclaredField("activity");
                activityField.setAccessible(true);
                return (T) activityField.get(activityClientRecord);
            }
        }
        throw new Exception("请确认app界面已经打开，并且手机没有熄屏。");
    }

    public static View findViewById(int id) throws Exception {
        Activity activity = getTopActivity();
        View view = activity.findViewById(id);
        if (view != null) {
            return view;
        }
        List<Object> fragments = getFragments();
        if (fragments != null) {
            for (Object fragment : fragments) {
                try {
                    View fragmentView = (View) ReflectUtils.forceInvoke(fragment, "getView");

                    view = fragmentView.findViewById(id);
                    if (view != null) {
                        return view;
                    }
                } catch (Exception ignored) {
                }
            }
        }
        return null;
    }


    public static List<Object> getFragments() {
        try {
            Activity topActivity = getTopActivity();
            Object fm = ReflectUtils.forceInvoke(topActivity, "getSupportFragmentManager");
            return (List<Object>) ReflectUtils.forceInvoke(fm, "getFragments");
        } catch (Exception ignored) {
        }
        return null;
    }

    public static <T extends Application> T getApplication() throws Exception {
        @SuppressLint("PrivateApi") Class<?> activityThread = Class.forName("android.app.ActivityThread");
        @SuppressLint("DiscouragedPrivateApi") Method currentApplication = activityThread.getDeclaredMethod("currentApplication");
        @SuppressLint("DiscouragedPrivateApi") Method currentActivityThread = activityThread.getDeclaredMethod("currentActivityThread");
        Object current = currentActivityThread.invoke((Object) null);
        Object app = currentApplication.invoke(current);
        return (T) app;
    }

    public static String viewTree() throws Exception {
        Activity activity = getTopActivity();
        Resources resources = getApplication().getResources();
        ObjectNode jsonNodes = viewTreeScan(activity.getWindow().getDecorView(), 0, resources);
        return JsonUtils.toString(jsonNodes);
    }

    private static ObjectNode viewTreeScan(View decorView, int deep, Resources resources) throws Exception {
        ObjectNode objectNode = JsonUtils.newObjectNode();
        objectNode.put("ViewClass", decorView.getClass().getName());
        objectNode.put("ViewId", decorView.getId());
        try {
            String name = resources.getResourceEntryName(decorView.getId());
            objectNode.put("ViewIdName", name);
        } catch (Resources.NotFoundException ignored) {
        }
        objectNode.put("IsClickable", decorView.isClickable());
        objectNode.put("IsVisible", decorView.getVisibility() == View.VISIBLE);
        objectNode.put("IsEnabled", decorView.isEnabled());
        objectNode.put("IsFocusable", decorView.isFocusable());
        objectNode.put("IsFocused", decorView.isFocused());
        objectNode.put("IsHorizontalScrollBarEnabled", decorView.isHorizontalScrollBarEnabled());
        objectNode.put("IsLongClickable", decorView.isLongClickable());
        objectNode.put("IsSelected", decorView.isSelected());
        objectNode.put("IsShown", decorView.isShown());
        objectNode.put("Width", decorView.getWidth());
        objectNode.put("Height", decorView.getHeight());
        objectNode.put("X", decorView.getX());
        objectNode.put("Y", decorView.getY());
        objectNode.put("ViewDeep", deep);
        if (decorView instanceof TextView) {
            objectNode.put("ViewText", ((TextView) decorView).getText().toString());
        } else if (decorView instanceof ViewGroup) {
            ArrayNode arrayNode = JsonUtils.newArrayNode();
            ViewGroup viewGroup = (ViewGroup) decorView;
            int newDeep = deep + 1;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                View childView = viewGroup.getChildAt(i);
                ObjectNode newRoot = viewTreeScan(childView, newDeep, resources);
                arrayNode.add(newRoot);
            }
            objectNode.set("ChildViews", arrayNode);
        }
        return objectNode;
    }

}
