package com.hujinwen.utils;

import org.junit.Test;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

public class ReflectUtilsTest {

    class A {
        public String aName;

        private void methodA() {
            System.out.println("执行了A方法");
        }

        public A(String aName) {
            this.aName = aName;
        }
    }

    class B extends A {

        public B(String aName) {
            super(aName);
        }
    }

    @Test
    public void test() throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {



        B b = new B("");
        Type genericSuperclass = b.getClass().getGenericSuperclass();
        Method methodA = b.getClass().getSuperclass().getDeclaredMethod("methodA");
        if (!methodA.isAccessible()) {
            methodA.setAccessible(true);
        }
        Object invoke = methodA.invoke(b);
        System.out.println();

    }


}