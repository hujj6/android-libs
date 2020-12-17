package com.hujinwen.kuaishou;

import android.app.Activity;
import android.widget.EditText;
import android.widget.HorizontalScrollView;

import androidx.recyclerview.widget.RecyclerView;

import com.kuaishou.android.model.feed.VideoFeed;
import com.kuaishou.android.model.mix.PhotoMeta;
import com.kuaishou.live.core.show.liveslidesquare.sidebar.response.LiveSquareSideBarFeedResponse;
import com.yxcorp.gifshow.HomeActivity;
import com.yxcorp.gifshow.detail.PhotoDetailActivity;
import com.yxcorp.gifshow.entity.QPhoto;
import com.yxcorp.gifshow.profile.activity.UserProfileActivity;
import com.yxcorp.plugin.search.SearchActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

import gz.com.alibaba.fastjson.JSON;
import gz.com.alibaba.fastjson.JSONObject;
import gz.httpserver.EmbeddHTTPServer;
import gz.radar.Android;
import gz.radar.AndroidUI;
import gz.util.XLog;

/**
 * 包含快手直播页获取
 */
@SuppressWarnings("ResourceType")
public class UIMocker {

    private static final ReentrantLock reentrantLock = new ReentrantLock(true);

    private static volatile long lastDownloadTime;

    public static java.util.List<Object> feedResults;

    public static java.util.List<Object> userProfileResults = new ArrayList<>();

    public static final java.util.List<Object> onlineLives = new ArrayList<>();

    private static volatile java.util.List<Object> onlineLivesCache = new ArrayList<>();

    private static final Semaphore SEMAPHORE = new Semaphore(1);

    private static boolean embeddHTTPServerStarted = false;


    private static EmbeddHTTPServer embeddHTTPServer = new EmbeddHTTPServer(6306) {
        @Override
        public String onResponse(String path, EmbeddHTTPParams embeddHTTPParams) throws Exception {
            //同时支持get 和 post 参数的获取
            if (path.contains("/isIdle")) {
                if (reentrantLock.isLocked() || !waitOk()) {
                    return "false";
                } else {
                    return "true";
                }
            }
            String keyword = embeddHTTPParams.getParameter("keyword");
            String type = embeddHTTPParams.getParameter("type");
            if (keyword == null) {
                return "缺少必要参数";
            }
            if (reentrantLock.isLocked()) {
                return "当前有任务在执行";
            }
            Exception ex = null;
            try {
                reentrantLock.lock();
                if ("composite".equals(type)) {
                    return JSON.toJSONString(searchFeed(keyword));
                } else if ("live".equals(type)) {
                    SEMAPHORE.acquire();
                    String respContent = JSON.toJSONString(onlineLivesCache);
                    SEMAPHORE.release();
                    return respContent;
                }
            } catch (Exception e) {
                ex = e;
            } finally {
                reentrantLock.unlock();
                if (ex != null) {
                    throw ex;
                }
            }
            return "";
        }
    };


    private static void updateLastDownloadTime() {
        lastDownloadTime = System.currentTimeMillis();
    }

    private static boolean waitOk() {
        return (System.currentTimeMillis() - lastDownloadTime) > 1000 * 30;
    }


    public static synchronized void startHttpServer() throws IOException {
        if (!embeddHTTPServerStarted) {
            embeddHTTPServer.start();
        }

    }

    public static synchronized void stopHttpServer() throws IOException {
        if (embeddHTTPServerStarted) {
            embeddHTTPServer.stop();
        }
    }


    public static void onResponse(x0.a0 input, b1.a0 output) {
        String url = input.a.url.i;
        try {
            if (url.contains("/search/feed?") || url.contains("/feed/profile2") || url.contains("/feed/hot?")) {
                if (feedResults != null) {
                    feedResults.add(output.b);
                }
            } else if (url.contains("/user/profile/v2?")) {
                if (userProfileResults != null) {
                    userProfileResults.add(output.b);
                }
            } else if (url.contains("/live/feed/square/more?") || url.contains("/live/feed/square/refresh?")) {
                KwaiRequest kwaiRequest = new KwaiRequest(input);
                if ("2".equals(kwaiRequest.getEncodedValue("tabId"))) {
                    k.a.b0.u.c uc = (k.a.b0.u.c) output.b;
                    LiveSquareSideBarFeedResponse liveSquareSideBarFeedResponse = (LiveSquareSideBarFeedResponse) uc.a;
                    for (QPhoto photo : liveSquareSideBarFeedResponse.mFeeds) {
                        com.kuaishou.android.model.feed.LiveStreamFeed liveStreamFeed = (com.kuaishou.android.model.feed.LiveStreamFeed) photo.getEntity();
                        JSONObject data = new JSONObject();
                        data.put("user", liveStreamFeed.mUser);
                        data.put("livePlayConfig", liveStreamFeed.mConfig);
                        onlineLives.add(data);
                    }
                }
            }
        } catch (Exception e) {
            XLog.appendText(e);
        }
    }

    public static java.util.List<Object> getOnlineLives() {
        return onlineLives;
    }

    public static Thread crawlLiveThread = null;

    public static void startCrawlLive() throws Exception {
        if (crawlLiveThread != null) {
            return;
        }
        crawlLiveThread = new Thread() {
            @Override
            public void run() {
                while (true) {
                    try {
                        crawlLive();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    try {
                        Thread.sleep(1000 * 60);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    try {
                        // 更新粉丝数
                        int i = 0;
                        for (Object obj : onlineLives) {
                            // just for test
                            if (i++ > 50) {
                                break;
                            }
                            //
                            JSONObject data = (JSONObject) obj;
                            com.kuaishou.android.model.user.User user = (com.kuaishou.android.model.user.User) data.get("user");
                            if (user != null) {
                                String userId = user.mId;
                                user.mText = getFansCount(userId);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        crawlLiveThread.start();
    }

    private static String getFansCount(String userId) throws Exception {
        UIMocker4User.startUserProfileActivity(userId);
        Thread.sleep(4000);
        android.view.View view = AndroidUI.findViewById(2131298742);
        if (view instanceof android.widget.TextView) {
            return ((android.widget.TextView) view).getText().toString();
        }
        return "0";
    }

    public static void scollRecyclerView(int index) throws Exception {
        java.util.List<android.view.View> views = AndroidUI.findViewsById(AndroidUI.getRootViewGroup(), 2131302331);
        final RecyclerView recyclerView = (RecyclerView) views.get(index);
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
            }
        });
    }

    public static void crawlLive() throws Exception {
        if (Android.getTopActivity() instanceof PhotoDetailActivity) {
            AndroidUI.finishCurrentActivity();
            Thread.sleep(1000);
        }
        final android.view.View menuBtn = AndroidUI.findViewById(2131300267);
        menuBtn.post(new Runnable() {
            @Override
            public void run() {
                menuBtn.performClick();
            }
        });
        Thread.sleep(1000);
        AndroidUI.clickByText("直播广场");
        Thread.sleep(3000);

        SEMAPHORE.acquire();
        onlineLivesCache.clear();
        onlineLivesCache.addAll(onlineLives);
        onlineLives.clear();
        SEMAPHORE.release();

        final android.view.View moreLive = AndroidUI.findViewById(2131302199);
        moreLive.post(new Runnable() {
            @Override
            public void run() {
                moreLive.performClick();
            }
        });
        Thread.sleep(3000);
        clickTab("音乐");
        Thread.sleep(3000);

        for (int i = 0; i < 20; i++) {
            scollRecyclerView(1);
            Thread.sleep(3000);
        }
        AndroidUI.finishCurrentActivity();
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
            AndroidUI.contextStartActivityForNewTask(SearchActivity.class.getName());
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

    private static void clickTab(String... containTexts) throws Exception {
        HorizontalScrollView tabs = Android.getTopActivity().findViewById(2131306210);
        for (String containText : containTexts) {
            final android.view.View view = AndroidUI.findViewByText(tabs, containText, true, true);
            if (view != null) {
                view.post(new Runnable() {
                    @Override
                    public void run() {
                        view.performClick();
                    }
                });
                return;
            }
        }
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
            VideoFeed entity = (VideoFeed) mPhoto.getEntity();
            //entity.mAccessorWrapper = null;
            JSONObject jsonSource = new JSONObject();
            jsonSource.put("mCommonMeta", entity.mCommonMeta);
            jsonSource.put("mCoverMeta", entity.mCoverMeta);
            jsonSource.put("mExtMeta", entity.mExtMeta);
            jsonSource.put("mPhotoMeta", entity.mPhotoMeta);
            jsonSource.put("mUser", entity.mUser);
            jsonSource.put("mTubeModel", entity.mTubeModel);
            jsonSource.put("mLivePlaybackMeta", entity.mLivePlaybackMeta);
            jsonSource.put("mVideoModel", entity.mVideoModel);
            jsonSource.put("kwaiId", kwaiVideo.kwaiId);
            jsonSource.put("title", kwaiVideo.title);
            jsonSource.put("mFansCount", kwaiVideo.userFansCount);
            kwaiVideo.jsonSource = jsonSource;
            try {
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

    private static String findLocelText() {
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

    public static java.util.List<KwaiVideo> searchFeed(final String keyword) throws Exception {
        updateLastDownloadTime();
        checkUI();
        feedResults = new ArrayList<>();
        Activity activity = Android.getTopActivity();
        if (activity != null && activity instanceof SearchActivity) {
            final EditText keywordEditText = activity.findViewById(2131298139);
            clickTab("视频", "作品");
            keywordEditText.post(new Runnable() {
                public void run() {
                    keywordEditText.setText(keyword);
                }
            });
            final android.widget.TextView textView = activity.findViewById(2131305141);
            Thread.sleep(500);
            textView.post(new Runnable() {
                public void run() {
                    textView.performClick();
                }
            });
            Thread.sleep(3000);
            String locelText = findLocelText();
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


}
