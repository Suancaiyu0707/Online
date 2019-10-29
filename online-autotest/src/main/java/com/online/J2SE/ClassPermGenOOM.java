package com.online.J2SE;

import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/***
 * -XX:+PrintGCDetails -XX:PermSize10m -XX:MaxPermSize10m -XX:TraceClassUnloading
 */
public class ClassPermGenOOM {
    public static Object Proxy(Class <?>targetClass){
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(targetClass);
        enhancer.setUseCache(false);
        enhancer.setCallback(new MethodInterceptor() {
            @Override
            public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
                return methodProxy.invokeSuper(o,objects);
            }
        });
        return enhancer.create();
    }

    public static void main(String[] args) {
        while (true)
                Proxy(ClassPermGenOOM.class);
    }
}
