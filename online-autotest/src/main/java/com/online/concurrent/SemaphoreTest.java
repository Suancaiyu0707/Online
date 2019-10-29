package com.online.concurrent;

import com.google.gson.Gson;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/***
 *  内部会维护一个信号量state,每次调用acquire，则state减1，如果当前state==0，则无法获得许可证，只能得别的线程调用release来释放许可证。
 *  release要释放一个许可证，这样等待的线程就可以获得许可证，继续执行
 */
public class SemaphoreTest {
    //定义一个包含了10个许可证的信号,内部会维护一个信号量state
    private static Semaphore semaphore = new Semaphore(10); // int state=10
    public static void main(String[] args) throws InterruptedException {

        Executor executor = Executors.newCachedThreadPool();
        for (int i = 0; i < 100; i++) {
            Thread.sleep(100);
            executor.execute(new Playground(semaphore,"线程_"+i));
        }

    }


}
class Playground implements Runnable{
    private Semaphore semaphore;
    private String name;
    public Playground(Semaphore semaphore,String name){
        this.semaphore = semaphore;
        this.name = name;
    }
    @Override
    public void run() {

        try {
            //这里如果state已经是0的话，就无法获得许可证，就会阻塞在这。如果不想阻塞的话，可以用tryAcquire,拿不到的直接返回false
            semaphore.acquire();//从semaphore中获得许可证 state=state-1
            System.out.println("semaphore="+new Gson().toJson(semaphore));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
            System.out.println(name+" 获得许可证========="+name);
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            semaphore.release();//释放许可证 state=state+1，别的等待的线程就可以继续拿到许可证，继续执行



    }
}