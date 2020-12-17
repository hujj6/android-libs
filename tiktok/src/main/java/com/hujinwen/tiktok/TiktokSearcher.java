package com.hujinwen.tiktok;

import android.app.Activity;
import android.view.View;
import android.widget.FrameLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.bytedance.ies.dmt.ui.widget.DmtTextView;
import com.bytedance.router.SmartRouter;
import com.hujinwen.tools.AndroidUI;
import com.ss.android.ugc.aweme.profile.ui.UserProfileActivity;


/**
 * 抖音hook爬虫
 * apk版本：12.6.0
 */
@SuppressWarnings("ResourceType")
public class TiktokSearcher {

    public static void searchUserFeed(final String secUserId) throws Exception {
        SmartRouter.buildRoute(AndroidUI.getTopActivity(), "aweme://user/profile?sec_uid=" + secUserId).open();
        slide();
    }

    public static void searchUser(final String keyword) throws Exception {
        StringBuilder sb = new StringBuilder();
        sb.append("\nkeyword -> " + keyword + "\n");

        SmartRouter.buildRoute(AndroidUI.getTopActivity(), "//search?keyword=" + keyword + "&display_keyword=" + keyword + "&enter_from=anywheredoor").open();


        try {
            Thread.sleep(3000);
            Activity activity = AndroidUI.getTopActivity();
            View viewById = activity.findViewById(2131174479);
            sb.append("\nviewById -> " + viewById + "\n");

            View view = AndroidUI.findViewByText(viewById, "用户");
            sb.append("\nview -> " + view + "\n");
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
                    sb.append("\nview -> " + view + "\n");
                } while (view != null);
            }
        } catch (Exception e) {
            throw new RuntimeException(sb.toString());
        }
        AndroidUI.getTopActivity().finish();
    }

    private static void slide() throws Exception {
        Thread.sleep(5000);
        Activity activity = AndroidUI.getTopActivity();
        AndroidUI.hover(300, 400, 100);
        Thread.sleep(1000);
        if (activity instanceof UserProfileActivity) {
            activity = AndroidUI.getTopActivity();
            final RecyclerView bb2 = activity.findViewById(2131168164);

            for (int i = 0; i < 500; i++) {
                // 判断是否结束了
                if (isEnd(bb2)) {
                    Thread.sleep(3000);
                    if (isEnd(bb2)) {
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
                Thread.sleep(2000);
            }
        }
    }

    /**
     * 抓取结束
     */
    private static boolean isEnd(RecyclerView bb2) {
        FrameLayout lastChild = (FrameLayout) bb2.getChildAt(bb2.getChildCount() - 1);
        if (lastChild.getChildCount() == 3) {
            View view = lastChild.getChildAt(1);
            if (view instanceof DmtTextView) {
                DmtTextView textView = (DmtTextView) view;
                return textView.getText().toString().contains("暂时没有更多了");
            }
        }
        return false;
    }
}


