package com.online.concurrent;

import java.util.Date;
import java.util.concurrent.locks.LockSupport;

public class ThreadTest3 {
    public static void main(String[] args) throws InterruptedException {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println(System.currentTimeMillis());
                    while(true){
                        if(Thread.currentThread().isInterrupted()){

                            break;
                        }
                        System.out.println("========");
                    }
                    System.out.println(System.currentTimeMillis()+"======");
            }
        });
        t.start();
        Thread.sleep(1000);
        t.interrupt();
    }
}
