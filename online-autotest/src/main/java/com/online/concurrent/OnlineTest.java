package com.online.concurrent;

public class OnlineTest {

    public static void main(String[] args)  {
        //获得执行当前main方法的线程
        Thread currentThread = Thread.currentThread();

        Thread newThread = new Thread(new MyRunnable());
        newThread.start();
        try {
            //启动当前线程，并且只有等待该线程执行完后，才能继续执行currentThread主线程
            newThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        Thread newThread2 = new Thread(new OnlinRunnable());
        newThread2.start();
        //用yield方法，表示当前线程currentThread会暂时让出cpu时间片给别的线程执行
        Thread.yield();
        for(int i =0;i<10;i++){
            //sleep会让当前线程睡眠挂起，如果这边有锁的话，它并不会释放它持有的锁。一旦挂起后，就不能被唤醒，但是会被中断
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("i am currentThread: i="+i);
        }
    }
}

/***
 * 通过实现Runnable一个线程，这种方式比较建议，因为支持实现多个接口
 */
class OnlinRunnable implements   Runnable{

    @Override
    public void run() {
        for(int i =0;i<10;i++){
            //sleep会让当前线程睡眠挂起，如果这边有锁的话，它并不会释放它持有的锁。一旦挂起后，就不能被唤醒，但是会被中断
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("============OnlinRunnable,i="+i);
        }

    }
}
