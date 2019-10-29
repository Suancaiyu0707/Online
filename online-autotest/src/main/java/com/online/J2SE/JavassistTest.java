//package com.online.J2SE;
//
//import javassist.ClassPool;
//import javassist.CtClass;
//import javassist.CtMethod;
//import javassist.NotFoundException;
//
//import java.net.URLClassLoader;
//
//public class JavassistTest {
//    private final static DynamicClassLoader CLASS_LOADER = new DynamicClassLoader((URLClassLoader) ASMTestMain.class.getClassLoader());
//
//    public static void main(String[] args) throws Exception {
//        CtClass ctClass = ClassPool.getDefault().get("com.online.J2SE.ForASMTestClass");
//        CtMethod ctMethod = ctClass.getDeclaredMethod("display1");
//
//        ctMethod.insertBefore("hello, 我开始增强代码");
//        ctMethod.insertAfter("hello, 我结束增强代码");
//
//        byte[] bytes = ctClass.toBytecode();
//        CLASS_LOADER.defineClassFromClassFile("com.online.J2SE.ForASMTestClass",bytes);
//        //重新加载字节码
//        Class<?> afterAMSClass = CLASS_LOADER.loadClass("com.online.J2SE.ForASMTestClass");
//
//        Object afterObject = afterAMSClass.newInstance();
//        afterAMSClass.getMethod("display1").invoke(afterObject);
//
//    }
//}
