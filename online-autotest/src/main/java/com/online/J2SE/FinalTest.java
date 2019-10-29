package com.online.J2SE;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

public class FinalTest extends Object{
    public static ReentrantLock lock = new ReentrantLock();
    public static List<FinalTest> list =new ArrayList<FinalTest>();
    public static void main(String[] args) {
        final String str="adada";//用final修饰的变量，不允许修改
        //str="bbbbb";//不合法行为
        lock.lock();//获得锁
        try{
            System.out.println("try。。。。。。");
        }catch(Exception e){

        }finally {//用finally修饰的代码基本会被调用，所以这边通常用来释放一些资源，比如锁、流等，只要这个线程被kill掉的话，那么finally就不会调用到
            System.out.println("finally。。。。。。");
            lock.unlock();
        }

        FinalTest t =new FinalTest();
        t=null;//取消t对堆内存的引用，所以这个是new出来的HashMapTest可以被回收了
        System.gc();//触发垃圾回收，可能会调用new出来的HashMapTest中的finalize方法

    }
    @Override
    protected void finalize() throws Throwable {//这个方法来源于
        list.add(this);
        System.out.println("list.size ="+list.size());
    }

}
