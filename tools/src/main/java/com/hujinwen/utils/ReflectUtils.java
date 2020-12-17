package com.hujinwen.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by hu-jinwen on 2020/7/22
 * <p>
 * 反射工具类
 */
public class ReflectUtils {

    private static final Method[] NO_METHODS = {};

    private static final Field[] NO_FIELDS = {};

    private static final Map<Class<?>, Field[]> declaredFieldsCache =
            new ConcurrentHashMap<>(256);

    private static final Map<Class<?>, Method[]> declaredMethodsCache =
            new ConcurrentHashMap<>(256);

    /**
     * 获取所有该类中定义的field
     */
    public static Field[] getDeclaredFields(Class<?> clazz) {
        Field[] result = declaredFieldsCache.get(clazz);
        if (result == null) {
            result = clazz.getDeclaredFields();
            declaredFieldsCache.put(clazz, (result.length == 0 ? NO_FIELDS : result));
        }
        return result;
    }

    /**
     * 获取所有该类中定义的method
     */
    public static Method[] getDeclaredMethods(Class<?> clazz) {
        Method[] result = declaredMethodsCache.get(clazz);
        if (result == null) {
            result = ArrayUtils.union(clazz.getDeclaredMethods(), findConcreteMethodsOnInterfaces(clazz));
            declaredMethodsCache.put(clazz, (result.length == 0 ? NO_METHODS : result));
        }
        return result;
    }

    /**
     * 查找指定 field 的 set 方法
     */
    public static Method findSetMethod(Class<?> clazz, Field field) {
        Class<?> searchType = clazz;
        while (searchType != null) {
            final Method[] methods = (searchType.isInterface() ? searchType.getMethods() : getDeclaredMethods(searchType));
            for (Method method : methods) {
                final String predictMethodName = predictSetMethodName(field);

                if (predictMethodName.equals(method.getName())) {
                    final Class<?>[] parameterTypes = method.getParameterTypes();
                    if (parameterTypes.length == 1 && parameterTypes[0] == field.getType()) {
                        return method;
                    }
                }
            }
            searchType = searchType.getSuperclass();
        }
        return null;
    }

    /**
     * 查找指定 field 的 get 方法
     */
    public static Method findGetMethod(Class<?> clazz, Field field) {
        Class<?> searchType = clazz;
        while (searchType != null) {
            final Method[] methods = (searchType.isInterface() ? searchType.getMethods() : getDeclaredMethods(searchType));
            for (Method method : methods) {
                final String predictMethodName = predictGetMethodName(field);

                if (predictMethodName.equals(method.getName()) && field.getType() == method.getReturnType()) {
                    return method;
                }
            }
            searchType = searchType.getSuperclass();
        }
        return null;
    }

    /**
     * 判断 method 是否是 公共的
     */
    public static boolean isPublic(Method method) {
        return "public".equals(Modifier.toString(method.getModifiers()));
    }

    /**
     * 获取接口中定义的方法
     */
    private static Method[] findConcreteMethodsOnInterfaces(Class<?> clazz) {
        Set<Method> result = new HashSet<>();
        for (Class<?> ifc : clazz.getInterfaces()) {
            for (Method ifcMethod : ifc.getMethods()) {
                if (!Modifier.isAbstract(ifcMethod.getModifiers())) {
                    result.add(ifcMethod);
                }
            }
        }
        final Method[] methods = new Method[result.size()];
        result.toArray(methods);
        return methods;
    }

    /**
     * 预测set方法名称
     */
    private static String predictSetMethodName(Field field) {
        final String fieldName = field.getName();
        char firstChar = fieldName.charAt(0);
        if (Character.isLowerCase(firstChar)) {
            firstChar = Character.toUpperCase(firstChar);
        }
        return "set" + firstChar + fieldName.substring(1);
    }

    /**
     * 预测get方法名称
     */
    private static String predictGetMethodName(Field field) {
        final String fieldName = field.getName();
        char firstChar = fieldName.charAt(0);
        if (Character.isLowerCase(firstChar)) {
            firstChar = Character.toUpperCase(firstChar);
        }
        return "get" + firstChar + fieldName.substring(1);
    }

    /**
     * 强行设置 field 的值
     * 无视 private、final
     *
     * @param obj       操作的对象
     * @param fieldName 字段名称
     * @param value     需要设置的值
     */
    public static void forceSet(Object obj, String fieldName, Object value) throws NoSuchFieldException, IllegalAccessException {
        final Field field = obj.getClass().getDeclaredField(fieldName);
        if (!field.isAccessible()) {
            field.setAccessible(true);
        }
        final Field modifiersField = Field.class.getDeclaredField("modifiers");
        if (!modifiersField.isAccessible()) {
            modifiersField.setAccessible(true);
        }
        modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

        field.set(obj, value);
    }

    public static Object forceInvoke(Object obj, String methodName) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        final Method method = obj.getClass().getDeclaredMethod(methodName);
        if (!method.isAccessible()) {
            method.setAccessible(true);
        }
        return method.invoke(obj);
    }

    /**
     * 强行执行某个方法
     *
     * @param obj         操作的对象
     * @param methodName  方法名称
     * @param paramsClazz 参数类型数组
     * @param params      方法执行传入的参数
     */
    public static Object forceInvoke(Object obj, String methodName, Class<?>[] paramsClazz, Object[] params) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        final Method method = obj.getClass().getDeclaredMethod(methodName, paramsClazz);
        if (!method.isAccessible()) {
            method.setAccessible(true);
        }
        return method.invoke(obj, params);
    }


}
