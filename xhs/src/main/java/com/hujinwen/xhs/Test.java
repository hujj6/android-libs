package com.hujinwen.xhs;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.RealCall;

/**
 * Created by hu-jinwen on 2021/8/24
 */
public class Test {

    public static void main(String[] args) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        OkHttpClient client = new OkHttpClient.Builder().build();
        client.interceptors().add(null);
        RealCall realCall = (RealCall) client.newCall(null);
    }

}
