package com.hujinwen.utils;

import java.lang.reflect.Array;

/**
 * Created by hu-jinwen on 2020/4/9
 * <p>
 * 数组相关工具，继承了 org.apache.commons.lang3.ArrayUtils，作为其补充
 */
public class ArrayUtils {


    @SafeVarargs
    @SuppressWarnings("unchecked")
    public static <T> T[] union(T[]... tts) {
        if (tts == null || tts.length == 0) {
            return null;
        }
        if (tts.length == 1) {
            return tts[0];
        }
        int countSum = 0;
        for (T[] ts : tts) {
            if (ts == null) {
                continue;
            }
            countSum += ts.length;
        }
        Class<?> componentType = tts.getClass().getComponentType().getComponentType();
        T[] result = (T[]) Array.newInstance(componentType, countSum);
        int addSum = 0;
        for (T[] ts : tts) {
            if (ts == null) {
                continue;
            }
            System.arraycopy(ts, 0, result, addSum, ts.length);
            addSum += ts.length;
        }
        return result;
    }


    public static byte[] union(byte[]... tts) {
        if (tts == null || tts.length == 0) {
            return null;
        }
        if (tts.length == 1) {
            return tts[0];
        }
        int countSum = 0;
        for (byte[] ts : tts) {
            if (ts == null) {
                continue;
            }
            countSum += ts.length;
        }
        byte[] result = new byte[countSum];
        int addSum = 0;
        for (byte[] ts : tts) {
            if (ts == null) {
                continue;
            }
            System.arraycopy(ts, 0, result, addSum, ts.length);
            addSum += ts.length;
        }
        return result;
    }


    public static short[] union(short[]... tts) {
        if (tts == null || tts.length == 0) {
            return null;
        }
        if (tts.length == 1) {
            return tts[0];
        }
        int countSum = 0;
        for (short[] ts : tts) {
            if (ts == null) {
                continue;
            }
            countSum += ts.length;
        }
        short[] result = new short[countSum];
        int addSum = 0;
        for (short[] ts : tts) {
            if (ts == null) {
                continue;
            }
            System.arraycopy(ts, 0, result, addSum, ts.length);
            addSum += ts.length;
        }
        return result;
    }


    public static int[] union(int[]... tts) {
        if (tts == null || tts.length == 0) {
            return null;
        }
        if (tts.length == 1) {
            return tts[0];
        }
        int countSum = 0;
        for (int[] ts : tts) {
            if (ts == null) {
                continue;
            }
            countSum += ts.length;
        }
        int[] result = new int[countSum];
        int addSum = 0;
        for (int[] ts : tts) {
            if (ts == null) {
                continue;
            }
            System.arraycopy(ts, 0, result, addSum, ts.length);
            addSum += ts.length;
        }
        return result;
    }


    public static long[] union(long[]... tts) {
        if (tts == null || tts.length == 0) {
            return null;
        }
        if (tts.length == 1) {
            return tts[0];
        }
        int countSum = 0;
        for (long[] ts : tts) {
            if (ts == null) {
                continue;
            }
            countSum += ts.length;
        }
        long[] result = new long[countSum];
        int addSum = 0;
        for (long[] ts : tts) {
            if (ts == null) {
                continue;
            }
            System.arraycopy(ts, 0, result, addSum, ts.length);
            addSum += ts.length;
        }
        return result;
    }


    public static char[] union(char[]... tts) {
        if (tts == null || tts.length == 0) {
            return null;
        }
        if (tts.length == 1) {
            return tts[0];
        }
        int countSum = 0;
        for (char[] ts : tts) {
            if (ts == null) {
                continue;
            }
            countSum += ts.length;
        }
        char[] result = new char[countSum];
        int addSum = 0;
        for (char[] ts : tts) {
            if (ts == null) {
                continue;
            }
            System.arraycopy(ts, 0, result, addSum, ts.length);
            addSum += ts.length;
        }
        return result;
    }

}
