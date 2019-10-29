package com.online.concurrent;

import com.google.gson.Gson;

import java.util.concurrent.*;

public class ThreadPoolTest {
    /***
     * 第一个参数 corePoolSize =2：表示线程池里核心线程数，线程池初始核心线程数是0，当任务过来的时候，如果线程池里的线程数少于corePoolSize,则会直接new一个线程，然后把任务交给新的线程执行。
     * 第二参数：maximumPoolSize =5：当workQueue里面的任务放满了，这个时候会判断当前线程池里的线程是否已经超过maximumPoolSize，如果还没超过，则会new新的线程来执行新来的任务，直到线程池里的线程数是maximumPoolSize(这个maximumPoolSize是包含核心线程数)
     * 第三个参数：keepAliveTime=100：如果当前线程池里的线程数超过corePoolSize，而且队列里没有任务在等待执行，这个时候空闲出来的线程(除了corePoolSize线程，其它都是空闲的)就会等待，等待到keepAliveTime秒后，还是没有任务，则直接销毁
     * 第六个参数：workQueue =new ArrayBlockingQueue(2)：它是一个阻塞队列，如果线程池里的线程数已经达到corePoolSize数，这个时候它会把task放到这个队列里ArrayBlockingQueue进行排队，当正在执行的线程执行完自己的任务后，就会从这个队列里拿任务执行
     */
    private static ThreadPoolExecutor executorService = new ThreadPoolExecutor(
            Runtime.getRuntime().availableProcessors(),Runtime.getRuntime().availableProcessors(),
            100,TimeUnit.SECONDS,
            new ArrayBlockingQueue (2));


    public static void main(String[] args) throws InterruptedException {
        for(int i=0;i<10;i++){
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println("==========");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            });
            //System.out.println(new Gson().toJson(executorService));
            ArrayBlockingQueue queue = (ArrayBlockingQueue) executorService.getQueue();
            System.out.println("pollSize="+executorService.getPoolSize());
            System.out.println("queue="+queue.size());

        }
        executorService.shutdown();
    }
}
