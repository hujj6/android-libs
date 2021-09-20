//package com.hujinwen.idlefish;
//
//import android.annotation.SuppressLint;
//import android.app.Activity;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.TextView;
//
//import gz.radar.Android;
//import gz.radar.AndroidUI;
//
///**
// * Created by hu-jinwen on 2020/9/21
// * <p>
// * 闲鱼搜索作品
// */
//@SuppressLint("ResourceType")
//public class SearchFeed {
//
//
//    /**
//     * 搜索商品
//     */
//    public static void searchGoods(final String keyword) throws Exception {
//        clickById(2131756110);
//        Thread.sleep(3000);
//
//        Activity activity = Android.getTopActivity();
//        final EditText keywordEditText = activity.findViewById(2131755622);
//        keywordEditText.post(new Runnable() {
//            public void run() {
//                keywordEditText.setText(keyword);
//            }
//        });
//
//        final TextView searchView = activity.findViewById(2131755469);
//        searchView.post(new Runnable() {
//            public void run() {
//                searchView.performClick();
//            }
//        });
//        Thread.sleep(3000);
//
//        for (int i = 0; i < 10; i++) {
//            AndroidUI.hover(300, 400, 100);
//            Thread.sleep(2000);
//        }
//
//        Android.getTopActivity().finish();
//    }
//
//
//    /**
//     * 点击搜索输入框
//     */
//    public static void clickById(int id) throws Exception {
//        View inputView = AndroidUI.findViewById(id);
//        if (inputView != null) {
//            do {
//                if (inputView.isClickable()) {
//                    final View clickView = inputView;
//                    inputView.post(new Runnable() {
//                        public void run() {
//                            clickView.performClick();
//                        }
//                    });
//                    Thread.sleep(2000);
//                    break;
//                }
//                inputView = (View) inputView.getParent();
//            } while (inputView != null);
//        }
//    }
//
//
//}
