package com.hujinwen;

import android.util.Log;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by hu-jinwen on 2021/12/6
 */
public class Test {

    public static void main(String[] args) {
        String stackTraceString = Log.getStackTraceString(new Throwable());
        System.out.println(stackTraceString);

    }


}

class S implements Interceptor {

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        "".equals(new Object());

        Request request = chain.request();

        return null;
    }
}
