package com.hujinwen.tiktok;

import android.app.Activity;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.bytedance.ies.dmt.ui.widget.DmtStatusView;
import com.bytedance.ies.dmt.ui.widget.DmtTextView;
import com.bytedance.router.SmartRouter;
import com.hujinwen.tools.AndroidUI;
import com.ss.android.ugc.aweme.search.activity.SearchResultActivity;

import java.util.List;


/**
 * 抖音直播
 * apk版本：12.2.0
 */
@SuppressWarnings("ResourceType")
public class TiktokLive {

//    public static void searchLive(final String keyword) throws Exception {
//        try {
//            SmartRouter.buildRoute(AndroidUI.getTopActivity(), "//search?keyword=" + keyword + "&display_keyword=" + keyword + "&enter_from=anywheredoor").open();
//
//            Thread.sleep(3000);
//            Activity activity = AndroidUI.getTopActivity();
//            View viewById = activity.findViewById(2131174479);
//
//            View view = AndroidUI.findViewByText(viewById, "直播");
//            if (view != null) {
//                do {
//                    if (view.isClickable()) {
//                        final View clickView = view;
//                        view.post(new Runnable() {
//                            public void run() {
//                                clickView.performClick();
//                            }
//                        });
//                        Thread.sleep(2000);
//                        break;
//                    }
//                    view = (View) view.getParent();
//                } while (view != null);
//            }
//            Thread.sleep(3000);
//            slide();
//        } catch (Exception e) {
//            throw new RuntimeException(e.getMessage(), e);
//        }
//    }
//
//    private static void slide() throws Exception {
//        StringBuilder sb = new StringBuilder();
//        try {
//            Activity activity = AndroidUI.getTopActivity();
//            if (activity instanceof SearchResultActivity) {
//                activity = AndroidUI.getTopActivity();
//
//                List<View> viewsById = AndroidUI.findViewsById(activity.getWindow().getDecorView(), 2131170444);
//
//                final RecyclerView view = (RecyclerView) viewsById.get(1);
//
//                for (int i = 0; i < 500; i++) {
//                    // 判断是否结束了
//                    if (isEnd(view)) {
//                        Thread.sleep(3000);
//                        if (isEnd(view)) {
//                            break;
//                        }
//                    }
//
//                    view.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            try {
//                                view.smoothScrollToPosition(view.getAdapter().getItemCount() - 1);
//                            } catch (Exception ignored) {
//                            }
//                        }
//                    });
//                    Thread.sleep(2000);
//                }
//            }
//        } catch (Exception e) {
//            throw new RuntimeException(e.getMessage(), e);
//        }
//    }

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


}
