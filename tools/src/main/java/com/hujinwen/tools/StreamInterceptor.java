package com.hujinwen.tools;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.zip.GZIPInputStream;

/**
 * Created by hu-jinwen on 2020/9/24
 * <p>
 * 搜索结果拦截器
 */
public class StreamInterceptor {

    /**
     * 截获 InputStream
     */
    public static Object[] interceptor(InputStream inputStream) throws IOException {
        if (!inputStream.markSupported()) {
            inputStream = new BufferedInputStream(inputStream);
        }
        inputStream.mark(50 * 1024 * 1024);

        GZIPInputStream gzipInputStream = new GZIPInputStream(inputStream);
        InputStreamReader inputStreamReader = new InputStreamReader(gzipInputStream);
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        StringBuilder sb = new StringBuilder();

        String line;
        while (!isEmpty(line = bufferedReader.readLine())) {
            sb.append(line);
        }
        inputStream.reset();
        return new Object[]{inputStream, sb.toString()};
    }

    /**
     * 截获 Reader
     */
    public static Object[] interceptor(Reader reader) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(reader);
        bufferedReader.mark(50 * 1024 * 1024);

        StringBuilder sb = new StringBuilder();
        String line;
        while (!isEmpty(line = bufferedReader.readLine())) {
            sb.append(line);
        }

        bufferedReader.reset();
        return new Object[]{bufferedReader, sb.toString()};
    }


    private static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

}
