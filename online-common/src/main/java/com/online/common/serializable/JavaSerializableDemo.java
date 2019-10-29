//package com.online.common.serializable;
//
//import org.junit.Test;
//import org.wit.ff.testmodel.SerializableUserA;
//
//import java.io.*;
//
///**
// *
// * <pre>
// * Java原生序列化机制.
// * 与方法无关.
// *
// * 安全.
// *
// * </pre>
// *
// * @author F.Fang
// * @version $Id: JavaSerializableDemo.java, v 0.1 2014年10月29日 上午12:48:11 F.Fang Exp $
// */
//public class JavaSerializableDemo {
//
//    /**
//     *
//     * <pre>
//     * Java自带序列化机制:检测对象序列化的内容.
//     * 序列化与方法无关,属性的赋值不通过方法.
//     * </pre>
//     *
//     */
//    @Test
//    public void test1() {
//        SerializableUserA userA = new SerializableUserA("nobody",18);
//        ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
//        try {
//            ObjectOutputStream oos = new ObjectOutputStream(baos);
//            oos.writeObject(userA);
//            oos.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        byte[] userABytes = baos.toByteArray();
//
//        ByteArrayInputStream bais = new ByteArrayInputStream(userABytes);
//        try {
//            ObjectInputStream ois = new ObjectInputStream(bais);
//            SerializableUserA userAS = (SerializableUserA) ois.readObject();
//            //System.out.println(userAS);
//            assertEquals(userA,userAS);
//        } catch (IOException e) {
//            e.printStackTrace();
//            assertFalse(true);
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//            assertFalse(true);
//        }
//    }
//
//    /**
//     *
//     * <pre>
//     * Java自带序列化机制:序列化和反序列化的类不同.
//     * (包括包和类名不同)
//     * java.lang.ClassCastException.
//     * </pre>
//     *
//     */
//    @Test
//    public void test2() {
//        SerializableUserA userA = new SerializableUserA("nobody",18);
//        ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
//        try {
//            ObjectOutputStream oos = new ObjectOutputStream(baos);
//            oos.writeObject(userA);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        byte[] userABytes = baos.toByteArray();
//
//        ByteArrayInputStream bais = new ByteArrayInputStream(userABytes);
//        try {
//            ObjectInputStream ois = new ObjectInputStream(bais);
//            org.wit.ff.testmodel.ch.SerializableUserA userA1 = (org.wit.ff.testmodel.ch.SerializableUserA) ois
//                    .readObject();
//            System.out.println(userA1);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        } catch(java.lang.ClassCastException e){
//            e.printStackTrace();
//            assertTrue(true);
//        }
//    }
//
//    /**
//     *
//     * <pre>
//     * 使用protobuff执行序列化.
//     * </pre>
//     *
//     */
//    @Test
//    public void test3(){
//        SerializableUserA userA = new SerializableUserA("nobody",18);
//        byte[] arr = ProtoStuffSerializerUtil.serialize(userA);
//        SerializableUserA userAs = ProtoStuffSerializerUtil.deserialize(arr, SerializableUserA.class);
//        //System.out.println(userAs);
//        assertNotNull(userAs);
//        assertEquals(userA.getAge(),userAs.getAge());
//        assertEquals(userA.getName(),userAs.getName());
//    }
//
//    /**
//     *
//     * <pre>
//     * 使用Protobuff进行序列化.
//     * 序列化时的类和反序列化的类包路径不同.
//     * 反序列化成功.
//     * </pre>
//     *
//     */
//    @Test
//    public void test4(){
//        SerializableUserA userA = new SerializableUserA("nobody",18);
//        byte[] arr = ProtoStuffSerializerUtil.serialize(userA);
//        org.wit.ff.testmodel.ch.SerializableUserA userAs = ProtoStuffSerializerUtil.deserialize(arr, org.wit.ff.testmodel.ch.SerializableUserA.class);
//        //System.out.println(userAs);
//        assertEquals(18,userAs.getAge());
//        assertEquals("nobody",userAs.getName());
//    }
//
//    /**
//     *
//     * <pre>
//     * 使用Protobuff进行序列化.
//     * 序列化时的类和反序列化的类属性相同,类名不同.
//     * 反序列化成功.
//     * </pre>
//     *
//     */
//    @Test
//    public void test5(){
//        SerializableUserA userA = new SerializableUserA("nobody",18);
//        byte[] arr = ProtoStuffSerializerUtil.serialize(userA);
//        SerializableUserB userBs = ProtoStuffSerializerUtil.deserialize(arr, SerializableUserB.class);
//        // System.out.println(userBs);
//        assertEquals(18,userBs.getAge());
//        assertEquals("nobody",userBs.getName());
//    }
//
//    /**
//     *
//     * <pre>
//     * 使用Protobuff进行序列化.
//     * 序列化时的类的属性都包含在反序列化的类属性中.
//     * 反序列化成功.
//     * </pre>
//     *
//     */
//    @Test
//    public void test6(){
//        SerializableUserA userA = new SerializableUserA("nobody",18);
//        byte[] arr = ProtoStuffSerializerUtil.serialize(userA);
//        org.wit.ff.testmodel.ch1.SerializableUserA userAs = ProtoStuffSerializerUtil.deserialize(arr, org.wit.ff.testmodel.ch1.SerializableUserA.class);
//        // System.out.println(userAs);
//        assertEquals(18,userAs.getAge());
//        assertEquals("nobody",userAs.getName());
//    }
//
//    /**
//     *
//     * <pre>
//     * 使用Protobuff进行序列化.
//     * 序列化时的类和反序列化的类完全不同,属性名称也不相同,类型一致.
//     * 反序列化成功.
//     * </pre>
//     *
//     */
//    @Test
//    public void test7(){
//        SerializableUserA userA = new SerializableUserA("nobody",18);
//        byte[] arr = ProtoStuffSerializerUtil.serialize(userA);
//        org.wit.ff.testmodel.ch1.SerializableUserB userBs = ProtoStuffSerializerUtil.deserialize(arr, org.wit.ff.testmodel.ch1.SerializableUserB.class);
//        System.out.println(userBs);
//        assertEquals("nobody",userBs.getNoneName());
//        assertEquals(18,userBs.getNoneAge());
//    }
//
//    /**
//     *
//     * <pre>
//     * 使用Protobuff进行序列化.
//     * 序列化时的类和反序列化的类完全不同,属性名称也不相同.
//     * 各属性类型均不匹配.
//     * 针对属性
//     * 如果序列化类型为int 8
//     * 反序列化的类型为long
//     * 序列化成功.
//     * 反序列化的类型为double
//     * 反序列化不成功.
//     * eg1:
//     * SerializableUserA :age int,name String
//     * SerializableUserC : noneAge double, noneName String
//     * 反序列化不成功.
//     * SerializableUserC : noneAge long, noneName String
//     * 反序列化成功.
//     * </pre>
//     *
//     */
//    @Test
//    public void test8(){
//        SerializableUserA userA = new SerializableUserA("nobody",18);
//        byte[] arr = ProtoStuffSerializerUtil.serialize(userA);
//        org.wit.ff.testmodel.ch1.SerializableUserC userCs = ProtoStuffSerializerUtil.deserialize(arr, org.wit.ff.testmodel.ch1.SerializableUserC.class);
//        System.out.println(userCs);
//        assertNotNull(userCs);
//        // 属性类型不匹配时发生异常!
//        try{
//            ProtoStuffSerializerUtil.deserialize(arr, org.wit.ff.testmodel.ch.SerializableUserC.class);
//        }catch(Exception e){
//            e.printStackTrace();
//            assertTrue(true);
//        }
//
//    }
//}