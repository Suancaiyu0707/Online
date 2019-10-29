package com.online.J2SE;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class UserThreadPool {
    public static void main(String[] args) {
        //续存队手动设置固定长度为 2. 为了快速触发 rejectHandler
        BlockingQueue queue = new LinkedBlockingQueue( 2 );

        UserThreadFactory factory1 = new UserThreadFactory("第一机房");
        UserThreadFactory factory2 = new UserThreadFactory("第二机房");

        UserRejectHandler handler = new UserRejectHandler();

        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(1,2,60,TimeUnit.SECONDS,queue,factory1,handler);
        ThreadPoolExecutor threadPoolExecutor2 = new ThreadPoolExecutor(1,2,60,TimeUnit.SECONDS,queue,factory2,handler);

        Runnable task = new Task();

        for(int i=0;i<200;i++){
            threadPoolExecutor.execute(task);
            threadPoolExecutor2.execute(task);
        }


    }
}
