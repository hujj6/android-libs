package com.hujinwen.kuaishou;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.RecyclerView;

import com.kuaishou.android.model.feed.VideoFeed;
import com.kuaishou.android.model.mix.PhotoMeta;
import com.yxcorp.gifshow.HomeActivity;
import com.yxcorp.gifshow.entity.QPhoto;
import com.yxcorp.gifshow.model.response.UserProfileResponse;
import com.yxcorp.gifshow.profile.activity.UserProfileActivity;
import com.yxcorp.plugin.search.SearchActivity;
import com.yxcorp.utility.RomUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import gz.com.alibaba.fastjson.JSON;
import gz.com.alibaba.fastjson.JSONObject;
import gz.radar.Android;
import gz.radar.AndroidUI;
import gz.util.XFile;
import gz.util.XLog;

/**
 * 包含抓用户profile信息
 */
@SuppressWarnings("ResourceType")
public class UIMocker4User {

    static java.util.List<Object> feedResults;

    static Object userProfileResult;

    public static void onResponse(x0.a0 input, b1.a0 output) {
        String url = input.a.url.i;
        try {
            if (url.contains("/search/feed?") || url.contains("/feed/profile2") || url.contains("/feed/hot?")) {
                if (feedResults != null) {
                    feedResults.add(output.b);
                }
            } else if (url.contains("/user/profile/v2?")) {
                userProfileResult = output.b;
            }
        } catch (Exception e) {
            XLog.appendText(e);
        }
    }

    private static void checkUI() throws Exception {
        Activity activity = Android.getTopActivity();
        if (activity == null) {
            return;
        }
        if (activity instanceof UserProfileActivity) {
            AndroidUI.finishCurrentActivity();
            Thread.sleep(1000);
        } else if (activity instanceof HomeActivity) {
            AndroidUI.startActivity(SearchActivity.class.getName());
            Thread.sleep(2000);
            activity = Android.getTopActivity();
            final EditText keywordEditText = activity.findViewById(2131298139);
            keywordEditText.post(new Runnable() {
                @Override
                public void run() {
                    keywordEditText.performClick();
                }
            });
            Thread.sleep(500);
            keywordEditText.post(new Runnable() {
                @Override
                public void run() {
                    keywordEditText.setText("罗志祥");
                }
            });
            final android.widget.TextView textView = activity.findViewById(2131305141);
            textView.post(new Runnable() {

                public void run() {
                    textView.performClick();
                }
            });
            Thread.sleep(3000);
        }
    }

    private static void clickTab(Activity activity, String... containTexts) {
        HorizontalScrollView tabs = activity.findViewById(2131306210);
        LinearLayout linearLayout = (LinearLayout) tabs.getChildAt(0);
        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            final android.view.View childView = linearLayout.getChildAt(i);
            if (childView instanceof android.widget.TextView) {
                String text = ((android.widget.TextView) childView).getText().toString();
                for (String containText : containTexts) {
                    if (text.contains(containText)) {
                        childView.post(new Runnable() {
                            @Override
                            public void run() {
                                childView.performClick();
                            }
                        });
                        return;
                    }
                }
            }
        }
    }

    private static String findLocelText(android.view.View decorView) {
        for (Object feedResult : feedResults) {
            k.a.b0.u.c uc = (k.a.b0.u.c) feedResult;
            com.yxcorp.plugin.search.response.SearchResultResponse searchResultResponse = (com.yxcorp.plugin.search.response.SearchResultResponse) uc.a;
            for (int i = 0; i < searchResultResponse.mPhotos.size(); i++) {
                QPhoto mPhoto = searchResultResponse.mPhotos.get(i);
                try {
                    VideoFeed entity = (VideoFeed) mPhoto.getEntity();
                    PhotoMeta mPhotoMeta = entity.mPhotoMeta;
                    if (mPhotoMeta.mLikeCount > 100) {
                        return mPhotoMeta.mLikeCount + "";
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public static java.util.List<KwaiVideo> parseKwaiVideos(java.util.List<QPhoto> mPhotos) throws Exception {
        java.util.List<KwaiVideo> videos = new ArrayList<>();
        for (int i = 0; i < mPhotos.size(); i++) {
            QPhoto mPhoto = mPhotos.get(i);
            KwaiVideo kwaiVideo = new KwaiVideo();
            kwaiVideo.title = mPhoto.getCaption();
            kwaiVideo.userName = mPhoto.getUserName();
            kwaiVideo.userId = mPhoto.getUserId();
            kwaiVideo.kwaiId = mPhoto.getKwaiId();
            kwaiVideo.userFansCount = mPhoto.getUser().mFansCount;
            kwaiVideo.videoUrl = mPhoto.getVideoUrl();
            kwaiVideo.videoDuration = mPhoto.getVideoDuration() + "";
            try {
                VideoFeed entity = (VideoFeed) mPhoto.getEntity();
                PhotoMeta mPhotoMeta = entity.mPhotoMeta;
                kwaiVideo.createTime = mPhotoMeta.mTime;
                kwaiVideo.likeCount = mPhotoMeta.mLikeCount + "";
                kwaiVideo.commentCount = mPhotoMeta.mCommentCount + "";
                kwaiVideo.viewCount = mPhotoMeta.mViewCount + "";
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                kwaiVideo.shareInfo = mPhoto.getCommonMeta().mShareInfo;
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (kwaiVideo.shareInfo != null) {
                videos.add(kwaiVideo);
            }
        }
        return videos;
    }

    public static java.util.List<KwaiVideo> searchFeed(final String keyword) throws Exception {
        feedResults = new ArrayList<>();
        checkUI();
        Activity activity = Android.getTopActivity();
        if (activity instanceof UserProfileActivity) {
            AndroidUI.finishCurrentActivity();
            Thread.sleep(1000);
            activity = Android.getTopActivity();
        }
        if (activity != null && activity instanceof SearchActivity) {
            final EditText keywordEditText = activity.findViewById(2131298139);
            keywordEditText.post(new Runnable() {
                @Override
                public void run() {
                    keywordEditText.setText(keyword);
                }
            });
            clickTab(activity, "视频", "作品");
            final android.widget.TextView textView = activity.findViewById(2131305141);
            Thread.sleep(500);
            textView.post(new Runnable() {

                public void run() {
                    textView.performClick();
                }
            });
            Thread.sleep(5000);
            String locelText = findLocelText(activity.getWindow().getDecorView());
            if (locelText != null) {
                android.view.View childView = AndroidUI.findViewByText(activity.getWindow().getDecorView(), locelText, true, false);
                android.view.View recyclerView = null;
                while (true && childView != null) {
                    childView = (android.view.View) childView.getParent();
                    if (childView instanceof RecyclerView) {
                        recyclerView = childView;
                        break;
                    }
                }
                if (recyclerView != null) {
                    final RecyclerView recyclerView2 = (RecyclerView) recyclerView;//activity.findViewById(2131304946);
                    int lastCount = recyclerView2.getAdapter().getItemCount();
                    for (int i = 0; i < 20; i++) {
                        final int count = lastCount;
                        recyclerView2.post(new Runnable() {
                            @Override
                            public void run() {
                                recyclerView2.smoothScrollToPosition(count - 1);
                            }
                        });
                        Thread.sleep(3000);
                        int currentCount = recyclerView2.getAdapter().getItemCount();
                        if (lastCount == currentCount) {
                            break;
                        }
                        lastCount = currentCount;
                    }
                }
            }
        }
        java.util.List<KwaiVideo> videoList = new ArrayList<>();
        for (Object feedResult : feedResults) {
            k.a.b0.u.c uc = (k.a.b0.u.c) feedResult;

            com.yxcorp.plugin.search.response.SearchResultResponse searchResultResponse = (com.yxcorp.plugin.search.response.SearchResultResponse) uc.a;
            videoList.addAll(parseKwaiVideos(searchResultResponse.mPhotos));
        }
        return videoList;
    }

    public static void findUserId() throws Exception {
        java.util.List<String> keywords = (XFile.readXiniFileLines("隔离防晒.txt"));
        java.util.Set<String> userIds = new HashSet<>(XFile.readXiniFileLines("userIds.txt"));
        final JSONObject userId2keyword = XFile.readXinitFileAsJSONObject("userId2keyword.txt");
        for (String line : keywords) {
            if (TextUtils.isEmpty(line)) {
                continue;
            }
            java.util.List<KwaiVideo> videoList = searchFeed(line);
            for (KwaiVideo kwaiVideo : videoList) {
                if (kwaiVideo.likeCount == null) {
                    continue;
                }
                int likeCount = 0;
                try {
                    likeCount = Integer.parseInt(kwaiVideo.likeCount);
                } catch (Exception e) {
                }
                if (likeCount > 50) {
                    if (userIds.contains(kwaiVideo.userId)) {
                        continue;
                    }
                    userId2keyword.put(kwaiVideo.userId, line);
                    userIds.add(kwaiVideo.userId);
                    XFile.writeXinitFile("userIds.txt", kwaiVideo.userId + "\n", true);
                    new Thread() {
                        @Override
                        public void run() {
                            try {
                                XFile.writeXinitFile("userId2keyword.txt", JSON.toJSONString(userId2keyword), false);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                }
            }
        }
    }


    public static void findData() throws Exception {
        JSONObject userId2keyword = XFile.readXinitFileAsJSONObject("userId2keyword.txt");
        java.util.Set<String> userIds = new HashSet<>(XFile.readXiniFileLines("userIds.txt"));
        for (String userId : userIds) {
            userProfileResult = null;
            startUserProfileActivity(userId);
            Thread.sleep(5000);
            if (userProfileResult != null) {
                k.a.b0.u.c uc = (k.a.b0.u.c) userProfileResult;
                UserProfileResponse userProfileResponse = (UserProfileResponse) uc.a;
                k.b.d.c.f.w userProfile = userProfileResponse.mUserProfile;
                KwaiUser kwaiUser = new KwaiUser();
                kwaiUser.wxNumber = "";
                kwaiUser.telephoneNumber = "";
                try {
                    kwaiUser.keyword = userId2keyword.getString(userId);
                    kwaiUser.fansNumber = userProfile.mOwnerCount.mFan;
                    kwaiUser.worksNumber = userProfile.mOwnerCount.mPhoto;
                    kwaiUser.kwaiId = userProfile.mProfile.mKwaiId;
                    kwaiUser.kwaiName = userProfile.mProfile.mName;
                    kwaiUser.location = userProfile.mCityName;
                    kwaiUser.sex = userProfile.mProfile.mSex;
                    kwaiUser.mText = userProfile.mProfile.mText;
                    Matcher matcher = Pattern.compile("[0-9a-zA-Z]{5,}").matcher(kwaiUser.mText);
                    if (matcher.find()) {
                        kwaiUser.wxNumber = matcher.group();
                    }
                    if (userProfile.mAdBusinessInfo != null && userProfile.mAdBusinessInfo.mAdProfilePhoneInfo != null && userProfile.mAdBusinessInfo.mAdProfilePhoneInfo.mPhoneList != null) {
                        if (userProfile.mAdBusinessInfo.mAdProfilePhoneInfo.mPhoneList.length > 0) {
                            kwaiUser.telephoneNumber = userProfile.mAdBusinessInfo.mAdProfilePhoneInfo.mPhoneList[0].mPhone;
                        }
                    }
                } catch (Exception e) {
                }
                if ((kwaiUser.telephoneNumber.length() > 0 || kwaiUser.wxNumber.length() > 0) && kwaiUser.fansNumber > 500 && kwaiUser.fansNumber < 150000) {
                    XFile.appendLine2XinitFile("users.txt", JSON.toJSONString(kwaiUser) + ",");
                }
            }
            if (Android.getTopActivity() instanceof UserProfileActivity) {
                AndroidUI.finishCurrentActivity();
                Thread.sleep(1000);
            }
        }
    }

    public static void startUserProfileActivity(String userId) throws Exception {
        userProfileResult = null;
        Activity activity = Android.getTopActivity();
        Uri uri = RomUtils.e("ks://profile" + '/' + userId);
        activity.startActivity(new Intent("android.intent.action.VIEW", uri).setPackage(activity.getPackageName()));
    }
}
