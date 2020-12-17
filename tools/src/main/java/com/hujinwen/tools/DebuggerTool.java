package com.hujinwen.tools;

import android.app.Activity;
import android.app.Instrumentation;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import androidx.recyclerview.widget.RecyclerView;


/**
 * Created by hu-jinwen on 12/2/20
 * <p>
 * 编写apk操作程序的调试工具。
 * 可以实现例如：
 * 通过id点击、输入文字
 * 滑动、滚动窗口
 */
public class DebuggerTool {

    /**
     * 通过 id 点击
     */
    public static void clickById(int id) throws Exception {
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
                    break;
                }
                view = (View) view.getParent();
            } while (view != null);
        }
    }

    /**
     * 强行点击一个按钮，通过 id
     * 不去判断该按钮是否可以点击
     */
    public static void forceClickById(int id) throws Exception {
        int[] positions = absViewPositionById(id);
        int x = positions[0];
        int y = positions[1];
        Instrumentation iso = new Instrumentation();
        iso.sendPointerSync(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(),
                MotionEvent.ACTION_DOWN, x, y, 0));
        iso.sendPointerSync(MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(),
                MotionEvent.ACTION_UP, x, y, 0));
    }

    /**
     * 通过 id，获取 view 在屏幕中的绝对路径
     */
    public static int[] absViewPositionById(int id) throws Exception {
        View view = AndroidUI.findViewById(id);
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        return location;
    }

    /**
     * 通过 id 输入内容
     */
    public static void sendKeywordById(int id, final String keyword) throws Exception {
        Activity activity = AndroidUI.getTopActivity();
        final EditText keywordEditText = activity.findViewById(id);
        keywordEditText.post(new Runnable() {
            @Override
            public void run() {
                keywordEditText.setText(keyword);
            }
        });
    }

    /**
     * 通过 id 滚动 RecyclerView
     */
    public static void scrollRecyclerViewById(int id) throws Exception {
        View viewById = AndroidUI.findViewById(id);

        if (!(viewById instanceof RecyclerView)) {
            return;
        }
        final RecyclerView recyclerView = (RecyclerView) viewById;
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                try {
                    recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
                } catch (Exception ignored) {
                }
            }
        });
    }

}
