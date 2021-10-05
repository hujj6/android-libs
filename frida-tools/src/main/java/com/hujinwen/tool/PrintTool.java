package com.hujinwen.tool;

import com.hujinwen.utils.ReflectUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by hu-jinwen on 2021/9/20
 */
public class PrintTool {

    /**
     * 打印出指定类中，所有方法
     */
    public static String printMethods(Class<?> clazz) {
        StringBuilder sb = new StringBuilder();
        sb.append("\nClass: ");
        sb.append(clazz.toString());
        sb.append("\n");

        Method[] methods = ReflectUtils.getMethods(clazz);
        for (Method method : methods) {
            sb.append(method.toString());
            sb.append("\n");
        }
        sb.append("\n");
        return sb.toString();
    }

    /**
     * 打印出指定对象中所有成员变量，以及变量的值
     */
    public static String printFields(Object obj) throws IllegalAccessException {
        StringBuilder sb = new StringBuilder();
        sb.append("\nObject: ");
        sb.append(obj.toString());
        sb.append("\n");

        Field[] fields = ReflectUtils.getFields(obj.getClass());
        for (Field field : fields) {
            sb.append(field.toString());
            sb.append(" => ");
            sb.append(field.get(obj));
            sb.append("\n");
        }
        sb.append("\n");
        return sb.toString();
    }


}
