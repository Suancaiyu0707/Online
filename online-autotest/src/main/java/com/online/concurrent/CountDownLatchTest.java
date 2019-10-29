package com.online.concurrent;

import com.google.gson.Gson;

import java.util.concurrent.CountDownLatch;

public class CountDownLatchTest {
    //CountDownLatch类似于发令枪，它表示要等待10个运动员都准备好了才能发令
    public static CountDownLatch latch = new CountDownLatch(10);

    public static void main(String[] args) throws InterruptedException {
        for(int i = 0;i<10;i++){
            Thread thread = new Thread(new CountDownLatchThread(i+"",latch));
            Thread.sleep(100);
            thread.start();
            latch.countDown();
        }
        //当调用这个时，它只有latch里参数的10被减到0之后，后面的输出才回继续执行
        //所以latch的作用就是等待，等待别的10个CountDownLatchThread线程都准备好后才继续执行
        //主线程等待发令，等待其它10个人都准备好了，才能发令
        latch.await();
        System.out.println("其它10个线程执行完了");
    }

}

class CountDownLatchThread implements Runnable{

    private String threadName ;

    public CountDownLatch getLatch() {
        return latch;
    }

    public void setLatch(CountDownLatch latch) {
        this.latch = latch;
    }

    private CountDownLatch latch;
    public String getThreadName() {
        return threadName;
    }

    public void setThreadName(String threadName) {
        this.threadName = threadName;

    }

    public CountDownLatchThread(String threadName,CountDownLatch latch){
        this.threadName = threadName;
        this.latch = latch;
    }

    @Override
    public void run() {
        //当调用这个时，会对latch中的10进行减1
        System.out.println(new Gson().toJson(latch));

        latch.countDown();

        System.out.println("threadName="+threadName);
    }
}
