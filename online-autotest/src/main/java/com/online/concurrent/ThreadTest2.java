package com.online.concurrent;

public class ThreadTest2 {
    public static void main(String[] args) {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    System.out.println(System.currentTimeMillis());
                    Thread.sleep(5000);
                    System.out.println("======="+System.currentTimeMillis());
                } catch (InterruptedException e) {
                    System.out.println("====555==="+System.currentTimeMillis());
                    e.printStackTrace();
                }
            }
        });

        t.start();

        //t.interrupt();
    }
}
