package com.online.J2SE;

/**
 * Created by IntelliJ IDEA.
 * Author: zhangjianan
 * Date: 8:37 PM 2019/3/7
 * Desc:
 **/
public class SynchronizeTest {
    private static Object obj = new Object();

    Thread t1 = new Thread(new Runnable() {//假设t1的threadId=1
        @Override
        public void run() {
            synchronized (obj){
                System.out.println("t1第一次拿到锁，会set锁对象头的 threadId=1");
            }//到这边，锁代码块已经执行完了
            System.out.println("t1第一次拿到锁后执行完代码释放锁，不会把锁对象头的threadId修改为空，所以这个时候锁的threadId还是旧的值threadId=1");
            synchronized (obj){//t1第二次再去尝试获得锁，这个时候先比较threadId是否跟上一次设置后保存在锁里的threadId相等
                System.out.println("t1第二次拿到锁，它会先判断锁对象头的threadId是否=1，如果是的话，就直接执行，不用set锁的threadId，所以说synchronized也叫偏向锁");
            }}
    });

    Thread t2 = new Thread(new Runnable() {//假设t2的threadId=2
        @Override
        public void run() {
            //t2尝试拿锁
            //（1）会先比较锁对象obj里的threadId是否等于当前线程t2的线程threadId-->2
            //  由于之前t1拿到偏向锁的时候，它会把obj锁里的threadId设置成1，所以当前里的threadId=1,跟当前线程t2里threadId并不相等（但是当前线程t1有可能还没执行完，有可能已经执行完会死掉了）
            // (2)线程t2会尝试使用CAS的操作，去把锁对象obj的threadId设置成当前线程id,解释设置成2
            //      a、线程t1其实已经执行结束，或者被kill掉，这个时候会修改线程id成功，threadId=2
            //      b、线程t1还没执行完，则这个时候线程t2肯定会CAS失败，这个时候t1发现有人来抢我这个偏向锁，为了保险起见，t1会设置锁对象里obj的线程指针，obj里的这个线程指针会被设置指向t1
            // （3）如果当t1把锁对象obj的头部的线程指针指向t1的时候，这个时候还有别的线程多次尝试CAS获得锁，这个时候，t1就赶紧把锁再次升级，取消别的线程的念想。
            synchronized (obj) {

            }//到这边，锁代码块已经执行完了
        }
        });
    }