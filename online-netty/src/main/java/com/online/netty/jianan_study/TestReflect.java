package com.online.netty.jianan_study;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class TestReflect {
    public static void main(String[] args) {
        Human human = new Human();
        human.setName("xuzf");
        human.setAge(29);
        human.setBirthday("1988-07-07");

        System.out.println(human.toString());
        System.out.println(toStringByRefelct(human));
    }

    public static String toStringByRefelct(Human human){
        StringBuffer buffer = new StringBuffer(1024);
        String className = human.getClass().getName();
        buffer.append(className+"{");
        Field[] fileds = human.getClass().getFields();//获得类的所有属性（注意这里是拿不到private属性）
        Method[] methods = human.getClass().getMethods();//获得类的所有set和get方法，对于private属性，可以用方法来反射拿到属性名称和值
        for(int i=0;i<fileds.length;i++){
            Field f = fileds[i];
            String filedName = f.getName();//获得属性名称 假设是xxx
            String methodName = "get"+filedName.substring(0,1).toUpperCase()+filedName.substring(1,filedName.length());//获取xxx对应的方法getXxx
            buffer.append(filedName+"=");
            try {
                Method method = human.getClass().getMethod(methodName,null);//通过反射，拿到方法对应的Method对象，因为getXxx没有参数，所以这边第二个参数传的是null
                try {
                    Object filedVal = method.invoke(human);//执行对象human的方法getXxx，就可以返回xxx的值
                    Class type = f.getType();//通过反射拿到属性的类型
                    //System.out.println(type.getName());
                    if(type.getName().equals("java.lang.String")){//判断xxx对应的类型，调用响应的toString方法
                        buffer.append((String)filedVal).append(",");
                    }
                    if(type.getName().equals("java.lang.Integer")){
                        buffer.append(((Integer)filedVal).intValue()).append(",");
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        buffer.append("}");
        System.out.println(buffer);
        return buffer.toString();
    }
}
