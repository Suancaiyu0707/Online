package com.online.J2SE;

import sun.misc.Signal;
import sun.misc.SignalHandler;

import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: zhifang.xu
 * Date: 2019/11/16
 * Time: 8:25 PM
 * Description: No Description
 * 通过signal优雅退出
 *      对于采用SignalHandler实现优雅退出的程序，在handler接口中一定要避免阻塞操作，否则它会导致已经注册的ShutdownHook无法执行，系统也无法退出
 *
 */
public class TestSignal {
    public static void main( String[] args ) throws InterruptedException {

        testSignale();
        TimeUnit.SECONDS.sleep(7);
        System.exit(0);


    }

    /***
     * 通过监听信号量，并注册SignalHandler的方式实现优化退出
     */
    public static void testSignale(){
        //如果是window系统，则选择SGIINT，接受CTRL+C中断的指令
        //否则选择接受SIGTERM（等价于kill pid）
//        String sigType =System.getProperties().getProperty("os.name")
//                .toLowerCase().startsWith("win")?"INT":"TERM";

        Signal signal = new Signal("INT");
        //将实例化后的SignalHandler注册到JDK的Signal，一旦java进程接收到kill pid或者ctrl+c，则回调SignalHandler
        Signal.handle(signal, new SignalHandler() {
            @Override
            public void handle( Signal signal ) {
                System.out.println("sginal 优雅退出");
            }
        });
    }
}
