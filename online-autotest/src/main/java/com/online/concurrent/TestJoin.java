package com.online.concurrent;

public class TestJoin {

    public static void main(String[] args) throws InterruptedException {
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    for(int i=0;i<5;i++){
                        Thread.sleep(1000);
                        System.out.println("t线程======="+System.currentTimeMillis());
                    }

                } catch (InterruptedException e) {
                    System.out.println("t线程====555==="+System.currentTimeMillis());
                    e.printStackTrace();
                }
            }
        });

        t.start();
        //调用join方法，则main线程只有让出cpu并等待t线程完成执行完后，才能继续执行join后的代码
        t.join();
        System.out.println("t线程执行完后，main线程开始继续执行======="+System.currentTimeMillis());

    }
}
