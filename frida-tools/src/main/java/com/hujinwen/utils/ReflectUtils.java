package com.hujinwen.utils;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by hu-jinwen on 2020/7/22
 * <p>
 * 反射工具类
 */
public class ReflectUtils {
    private static final Method NULL_METHOD;


    private static final Map<Class<?>, Field[]> DECLARED_FIELDS_CACHE =
            new ConcurrentHashMap<>(256);

    private static final Map<Class<?>, Field[]> FIELDS_CACHE =
            new ConcurrentHashMap<>(256);


    private static final Map<Class<?>, Method[]> DECLARED_METHODS_CACHE =
            new ConcurrentHashMap<>(256);

    private static final Map<Class<?>, Method[]> METHODS_CACHE =
            new ConcurrentHashMap<>(256);

    private static final Map<String, Method> SINGLE_METHOD_CACHE =
            new ConcurrentHashMap<>(256);


    static {
        try {
            NULL_METHOD = ReflectUtils.class.getDeclaredMethod("nullMethod");
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    private void nullMethod() {
    }

    /**
     * 获取所有该类中定义的field
     */
    public static Field[] getDeclaredFields(Class<?> clazz) {
        return DECLARED_FIELDS_CACHE.computeIfAbsent(clazz, k -> clazz.getDeclaredFields());
    }

    /**
     * 获取该类及其继承的所有的属性（包括私有属性）
     */
    public static Field[] getFields(Class<?> clazz) {
        if (!FIELDS_CACHE.containsKey(clazz)) {
            final List<Field> fieldList = extractFields(clazz);
            Field[] fields = new Field[fieldList.size()];
            FIELDS_CACHE.put(clazz, fieldList.toArray(fields));
        }
        return FIELDS_CACHE.get(clazz);
    }

    private static List<Field> extractFields(Class<?> clazz) {
        final List<Field> records = new ArrayList<>(ArrayUtils.asList(clazz.getDeclaredFields()));
        if (!clazz.isInterface() && clazz != Object.class) {
            records.addAll(extractFields(clazz.getSuperclass()));
            for (Class<?> iClazz : clazz.getInterfaces()) {
                records.addAll(extractFields(iClazz));
            }
        }
        return records;
    }

    /**
     * 获取对象属性值
     */
    private static Object getFieldValue(Object obj, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        return getFieldValue(obj.getClass(), obj, fieldName);
    }

    /**
     * 获取对象属性值
     * * 主动传入 Class，可解决获取父类属性的问题
     */
    private static Object getFieldValue(Class<?> clazz, Object obj, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        final Field field = clazz.getDeclaredField(fieldName);
        if (!field.isAccessible()) {
            field.setAccessible(true);
        }
        return field.get(obj);
    }


    /**
     * 获取所有该类中定义的method
     */
    public static Method[] getDeclaredMethods(Class<?> clazz) {
        return DECLARED_METHODS_CACHE.computeIfAbsent(clazz, k -> clazz.getDeclaredMethods());
    }


    /**
     * 获取该类以及其继承的所有方法（包括私有方法）
     */
    public static Method[] getMethods(Class<?> clazz) {
        if (!METHODS_CACHE.containsKey(clazz)) {
            final List<Method> methodList = extractMethods(clazz);
            final Method[] methods = new Method[methodList.size()];
            METHODS_CACHE.put(clazz, methodList.toArray(methods));
        }
        return METHODS_CACHE.get(clazz);
    }


    public static List<Method> extractMethods(Class<?> clazz) {
        final List<Method> records = new ArrayList<>();

        for (Method method : clazz.getDeclaredMethods()) {
            if (!Modifier.isAbstract(method.getModifiers())) {
                records.add(method);
            }
        }
        if (!clazz.isInterface() && clazz != Object.class) {
            records.addAll(extractMethods(clazz.getSuperclass()));
        }
        return records;
    }


    /**
     * 获取方法
     */
    public static Method getMethod(Class<?> clazz, String name, Class<?>... parameterTypes) {
        String methodMapKey = clazz.getName() + "." + name;
        Method method = SINGLE_METHOD_CACHE.get(methodMapKey);
        if (method == null) {
            try {
                method = clazz.getMethod(name, parameterTypes);
                SINGLE_METHOD_CACHE.put(methodMapKey, method);
            } catch (NoSuchMethodException e) {
                SINGLE_METHOD_CACHE.put(methodMapKey, NULL_METHOD);
            }
        } else if (method == NULL_METHOD) {
            method = null;
        }
        return method;
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
        return forceInvoke(obj, methodName, null, null);
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
        return forceInvoke(obj.getClass(), obj, methodName, paramsClazz, params);
    }

    public static Object forceInvoke(Class<?> clazz, Object obj, String methodName, Class<?>[] paramsClazz, Object[] params) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        final Method method = clazz.getDeclaredMethod(methodName, paramsClazz);
        return forceInvoke(obj, method, params);
    }

    public static Object forceInvoke(Object obj, Method method, Object... params) throws InvocationTargetException, IllegalAccessException {
        if (!method.isAccessible()) {
            method.setAccessible(true);
        }
        return method.invoke(obj, params);
    }


}
