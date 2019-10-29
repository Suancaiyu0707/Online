package com.online.concurrent;

import com.google.gson.Gson;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/****
 * CyclicBarrier：它维护一个final int parties
 *  await:调用await方法，线程会阻塞等待，直到parties==0后，才能继续执行
 *  countDown：调用该方法，parties就减1，直到parties=0后，就会唤醒调用await的线程
 */
public class CyclicBarrierTest {
    //barrier类型起跑线，10表示起跑线上的10个运动员都准备好了，才能开跑
    public static CyclicBarrier barrier = new CyclicBarrier(10);

    public static void main(String[] args) throws InterruptedException {
        for(int i = 0;i<10;i++){
            Thread thread = new Thread(new CyclicBarrierThread(i+"",barrier));
            Thread.sleep(1000);
            thread.start();
        }

        Thread.sleep(10000);

    }

}

class CyclicBarrierThread implements Runnable{

    private String threadName ;


    private CyclicBarrier barrier;

    public CyclicBarrier getBarrier() {
        return barrier;
    }

    public void setBarrier(CyclicBarrier barrier) {
        this.barrier = barrier;
    }

    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;

    }

    public CyclicBarrierThread(String threadName,CyclicBarrier barrier){
        this.threadName = threadName;
        this.barrier = barrier;
    }

    @Override
    public void run() {

        try {
            //当前线程会等待在这个barrier屏障，直到这个屏障门口凑足10个线程后，才会继续调用各个线程下面的await
            //await表示某个运动员指示自己准备好了，此时还是要等别的9个运动员都准备好
            //parties会减1
            System.out.println(new Gson().toJson(barrier));

            barrier.await();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (BrokenBarrierException e) {
            e.printStackTrace();
        }

        System.out.println("threadName="+threadName);

        barrier.reset();//barrier是可以重置，也就是重复利用的，CountDownLatch就不行
    }
}
