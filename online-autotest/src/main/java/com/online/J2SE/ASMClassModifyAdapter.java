//package com.online.J2SE;
//
//
//
//import com.sun.xml.internal.ws.org.objectweb.asm.ClassAdapter;
//import com.sun.xml.internal.ws.org.objectweb.asm.ClassVisitor;
//import com.sun.xml.internal.ws.org.objectweb.asm.MethodVisitor;
//import com.sun.xml.internal.ws.org.objectweb.asm.Opcodes;
//
//
//public class ASMClassModifyAdapter extends ClassAdapter {
//    public ASMClassModifyAdapter(ClassVisitor cv) {
//        super(cv);
//    }
//    @Override
//    public MethodVisitor visitMethod(int access,
//                                     String methodName,
//                                     String desc,
//                                     String signature,
//                                     String[] exceptions) {
//
//        if("display2".equals(methodName)){
//            return null;
//        }
//        if("display1".equals(methodName)){
//
//            MethodVisitor methodVisitor = cv.visitMethod(access, methodName, desc, signature, exceptions);
//            methodVisitor.visitCode();
//            //下面几行代码相当于对name属性赋值，相当于增加代码：name="我是name"
//            //加载this到栈顶
//            methodVisitor.visitVarInsn(Opcodes.AALOAD,0);
//            //ldc指令，从常量池中取出值加载到栈顶，这个代码会隐藏修改常量池
//            methodVisitor.visitLdcInsn("我是name");
//            //putfiled指令，修改ForASMTestClass的name属性
//            methodVisitor.visitFieldInsn(Opcodes.PUTFIELD,"com/online/J2SE/ForASMTestClass","name","Ljava/lang/String;");
//            //下面几行代码相当于对value属性赋值，相当于增加代码：value="我是value"
//            //加载this到栈顶
//            methodVisitor.visitVarInsn(Opcodes.AALOAD,0);
//            //ldc指令，从常量池中取出值加载到栈顶，这个代码会隐藏修改常量池
//            methodVisitor.visitLdcInsn("我是value");
//            //putfiled指令，修改ForASMTestClass的value属性
//            methodVisitor.visitFieldInsn(Opcodes.PUTFIELD,"com/online/J2SE/ForASMTestClass","value","Ljava/lang/String;");
//            //将添加一个属性获取并打印
//            //getstatic指令，获取System类的out属性
//            methodVisitor.visitFieldInsn(Opcodes.GETSTATIC,"java/lang/System","out","Ljava/io/PrintStream;");
//            //加载this到栈顶
//            methodVisitor.visitVarInsn(Opcodes.AALOAD,0);
//            //通过getfiled指令获取name属性的值并加载到栈顶
//            methodVisitor.visitFieldInsn(Opcodes.GETFIELD,"com/online/J2SE/ForASMTestClass","name","Ljava/lang/String;");
//            //调用out的println方法
//            methodVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL,"java/io/PrintStream","println","(Ljava/lang/String;)V");
//            methodVisitor.visitEnd();
//            return methodVisitor;
//        }else{//其余的不做任何处理，直接返回
//            return cv.visitMethod(access, methodName, desc, signature, exceptions);
//        }
//
//
//    }
//
//
//
//}
