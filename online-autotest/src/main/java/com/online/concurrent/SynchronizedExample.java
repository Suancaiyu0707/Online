package com.online.concurrent;

public class SynchronizedExample {


    int a =0;
    boolean flag = false;

    public synchronized void writer(){//获取锁
        a=1;
        flag = true;
        synchronized (this){
            System.out.println("========");
        }
    }//释放锁l

    public synchronized void reader(){//获取锁
        if(flag){
            int i =a ;
        }
    }//释放锁

    public void  hello(){
        synchronized (SynchronizedExample.class){
            System.out.println("========");
        }
    }

    public static void main(String[] args) {
        SynchronizedExample s1 = new SynchronizedExample();
        SynchronizedExample s2 = new SynchronizedExample();

        s1.writer();
        s1.reader();
       // s2.writer();

        //s1.hello();
       // s2.hello();
    }
}
