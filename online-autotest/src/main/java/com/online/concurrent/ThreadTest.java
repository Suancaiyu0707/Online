package com.online.concurrent;

import java.util.concurrent.*;

public class ThreadTest {


    private static ExecutorService executors = Executors.newFixedThreadPool(4);
    public static void threadTest() throws ExecutionException, InterruptedException {
        MyThread thread = new MyThread();
        //该方法会调用该thread绑定的硬件系统操作系统的的线程里的方法，从而执行run方法
        thread.start();
        //该方法会调用该MyRunnable绑定的硬件系统操作系统的的线程里的方法，从而执行run方法
        Thread t = new Thread(new MyRunnable());
        t.start();
        //通过创建一个Callable的任务，然后这个Callable交给线程池，线程池调用这个任务
        //这种写法，将任务资源统一管理，这样可以实现线程的复用，而且减少线程的创建和销毁的资源开销。注意，这个会返回一个结果集
        Future future =executors.submit(new Callable <Boolean>() {

            @Override
            public Boolean call() throws Exception {

                return null;
            }
        });
       // future.get();//可以通过get获得call返回的结果

        //通过创建一个FutureTask的任务，然后这个FutureTask交给线程池，线程池调用这个任务
        //这种写法，将任务资源统一管理，这样可以实现线程的复用，而且减少线程的创建和销毁的资源开销。注意，这个不会返回结果
        FutureTask future2 = new FutureTask(new Callable() {
            @Override
            public Object call() throws Exception {
                return null;
            }
        });
        executors.execute(future2);
        while(future2.isDone()){//只能不断循环的调用isDone判断线程是否执行完成

        }
        executors.shutdown();


    }
}

/***
 * 通过继承实现一个线程，这种方式不灵活，因为类只能支持单继承。
 * new一个线程的时候，底层是会绑定一个对应OS线程
 */
class MyThread extends  Thread{

    @Override
    public void run() {

        System.out.println("============myThread");

        SynchronizedExample synchronizedExample = new SynchronizedExample();
        synchronizedExample.reader();
    }

    public static void main(String[] args) {
        MyThread t = new MyThread();
        t.start();
        MyThread t1 = new MyThread();
        t1.start();
    }


}
/***
 * 通过实现Runnable一个线程，这种方式比较建议，因为支持实现多个接口
 */
class MyRunnable implements   Runnable{

    @Override
    public void run() {
        System.out.println("============myRunnable");
    }
}
