package com.hujinwen.utils;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    public static <T> List<T> asList(T... ts) {
        return Arrays.asList(ts);
    }

    public static List<Character> asList(char... chars) {
        final List<Character> list = new ArrayList<>(chars.length);
        for (char c : chars) {
            list.add(c);
        }
        return list;
    }


    public static List<Byte> asList(byte... bytes) {
        final List<Byte> list = new ArrayList<>(bytes.length);
        for (byte b : bytes) {
            list.add(b);
        }
        return list;
    }

    public static List<Short> asList(short... shorts) {
        final List<Short> list = new ArrayList<>(shorts.length);
        for (short s : shorts) {
            list.add(s);
        }
        return list;
    }

    public static List<Integer> asList(int... is) {
        final List<Integer> list = new ArrayList<>(is.length);
        for (int i : is) {
            list.add(i);
        }
        return list;
    }

    public static List<Long> asList(long... longs) {
        final List<Long> list = new ArrayList<>(longs.length);
        for (long l : longs) {
            list.add(l);
        }
        return list;
    }

    public static List<Boolean> asList(boolean... booleans) {
        final List<Boolean> list = new ArrayList<>(booleans.length);
        for (boolean b : booleans) {
            list.add(b);
        }
        return list;
    }

    public static Object[] newObjArray(Object... objects) {
        return objects;
    }


}
