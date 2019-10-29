package com.online.J2SE;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

public class UserThreadFactory implements ThreadFactory {
    private final String name;

    private final AtomicLong nextId =new AtomicLong(0L);
    public UserThreadFactory(String groupName ){
        name = "UserThreadFactory's "+ groupName+"-Worker-";
    }
    @Override
    public Thread newThread(Runnable task) {
        String threadName = name  + nextId.getAndIncrement();
        Thread thread = new Thread(null,task,threadName,0L);
        System.out.println(thread.getName());
        return thread;
    }
}

class Task implements Runnable{
    private final AtomicLong count =new AtomicLong(0L);

    @Override
    public void run() {
        System.out.println("running_"+count.getAndIncrement());

    }
}
