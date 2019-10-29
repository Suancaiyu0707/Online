//package com.online.J2SE;
//
//import com.sun.xml.internal.ws.org.objectweb.asm.ClassReader;
//import com.sun.xml.internal.ws.org.objectweb.asm.ClassWriter;
//
//import java.io.IOException;
//import java.net.URLClassLoader;
//
//public class ASMTestMain {
//    private final static DynamicClassLoader CLASS_LOADER = new DynamicClassLoader((URLClassLoader) ASMTestMain.class.getClassLoader());
//    public static void main(String[] args) throws Exception {
//
//        Class<?> beforeAMSClass = CLASS_LOADER.loadClass("com.online.J2SE.ForASMTestClass");
//        //修改字节码
//        CLASS_LOADER.defineClassFromClassFile("com.online.J2SE.ForASMTestClass",asmChangeClass());
//        //重新加载字节码
//        Class<?> afterAMSClass = CLASS_LOADER.loadClass("com.online.J2SE.ForASMTestClass");
//
//        Object beforeObject = beforeAMSClass.newInstance();
//        Object afterObject = afterAMSClass.newInstance();
//        beforeAMSClass.getMethod("display1").invoke(beforeObject);
//        afterAMSClass.getMethod("display1").invoke(afterObject);
//
//    }
//
//    private static byte[] asmChangeClass() throws IOException {
//        ClassReader classReader = new ClassReader("com.online.J2SE.ForASMTestClass");
//        ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_MAXS);
//        ASMClassModifyAdapter asmClassModifyAdapter=new ASMClassModifyAdapter(classWriter);
//        classReader.accept(asmClassModifyAdapter,ClassReader.SKIP_DEBUG);
//        return classWriter.toByteArray();
//    }
//}
