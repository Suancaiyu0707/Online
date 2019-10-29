package com.online.concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class CasCounter {
    private AtomicInteger atomicInteger = new AtomicInteger();

    private int i = 0;

    public static void main(String[] args) {
        final CasCounter counter = new CasCounter();
        List<Thread> ts = new ArrayList<Thread>(600);
        long start = System.currentTimeMillis();

        for(int i=0;i<100;i++){
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    for(int j=0;j<10000;j++){
                        counter.count();
                        counter.casCount();
                    }
                }
            });
            ts.add(thread);
        }
        for(Thread t :ts){
            t.start();
        }
        for(Thread t :ts){
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println(counter.i);
        System.out.println(counter.atomicInteger.get());
        System.out.println(System.currentTimeMillis()-start);
    }


    private void casCount(){
        for(;;){
            int i = atomicInteger.get();
            boolean suc = atomicInteger.compareAndSet(i,++i);
            if(suc){
                break;
            }
        }
    }
    private void count(){
        i++;
    }
}
