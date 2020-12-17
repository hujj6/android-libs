package com.hujinwen.utils;

/**
 * Created by hu-jinwen on 12/8/20
 * <p>
 * 对象相关的工具
 */
public class ObjectUtils {


    /**
     * 获取真实对象的class名称
     * （解决在js中有时获取对象class名称的异常）
     */
    public static String realClassName(Object obj) {
        return obj.getClass().getName();
    }

}
