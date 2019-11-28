package com.online.J2SE;

import sun.misc.Signal;
import sun.misc.SignalHandler;

import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: zhifang.xu
 * Date: 2019/11/16
 * Time: 8:09 PM
 * Description: No Description
 * 通过 addShutdownHook优雅退出
 * 注意：
 *      ShutdownHook在某些情况下不会被执行，比如JVM崩溃、无法接受信号量和Kill -9命令
 *      当存在多个ShutdownHook时，JVM无法保证它们的执行的先后顺序
 *      在JVM关闭期间不能动态添加或者删除ShutdownHook
 *      不能在ShutdownHook中调用System.exit(),它会卡住jvm，导致进程无法退出
 */
public class TestShutdownHook {
    public static void main( String[] args ) throws InterruptedException {
        System.out.println("=====begin=======");
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("jvm退出了");
            }
        }));

        TimeUnit.SECONDS.sleep(7);



    }
}
