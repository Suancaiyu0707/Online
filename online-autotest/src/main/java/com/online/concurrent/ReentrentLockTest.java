package com.online.concurrent;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrentLockTest {
    public static Executor executor = Executors.newFixedThreadPool(2);
    public static  final ReentrantLock lock = new ReentrantLock();
    final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static void main(String[] args) throws InterruptedException {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println(sdf.format(new Date())+"=====tttttt");

                    lock.lockInterruptibly();
                    Thread.sleep(30000000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    lock.unlock();
                }
            }
        });
        executor.execute(t);
         Thread t1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println(sdf.format(new Date())+"=====t1");
                    lock.lockInterruptibly();
                    //Thread.sleep(4000);
                    System.out.println(sdf.format(new Date())+"=====t1结束");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    lock.unlock();
                }
            }
        });
        Thread t2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println(sdf.format(new Date())+"=====t2");
                    lock.lockInterruptibly();
                    //Thread.sleep(4000);
                    System.out.println(sdf.format(new Date())+"=====t2结束");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    lock.unlock();
                }
            }
        });
        t1.start();
//        Thread.sleep(1000);
//       t2.start();
     //
          Thread.sleep(10000);
        System.out.println("=========开始t1.interrupt");
        t1.interrupt();
      //  Thread.sleep(10000);
       // LockSupport.unpark(t1);
       //




        System.out.println(sdf.format(new Date())+"=====1111");

    }

}
