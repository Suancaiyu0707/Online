package com.online.concurrent;

public class WaitTest {
    public static void main(String[] args) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                synchronized (WaitTest.class){
                    try {
                        WaitTest.class.wait();
                    } catch (InterruptedException e) {
                        System.out.println(Thread.currentThread());
                        e.printStackTrace();
                    }
                }

            }
        });

        t.start();
        Thread.currentThread().interrupt();
        t.interrupt();
    }

}
