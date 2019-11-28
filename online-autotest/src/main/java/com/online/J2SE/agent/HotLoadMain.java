package com.online.J2SE.agent;

import com.online.J2SE.classloader.HotLoad;
import com.sun.tools.attach.AgentInitializationException;
import com.sun.tools.attach.AgentLoadException;
import com.sun.tools.attach.VirtualMachine;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class HotLoadMain {
    public static void main(String[] args)
            throws ClassNotFoundException, InterruptedException, IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException, IOException, AgentLoadException, AgentInitializationException {
        while (true) {
            HotLoad account = new HotLoad();
            account.operation();

            account.operation3();
            Thread.sleep(5000);
        }
//程序启动之后，通过某种特定的手段加载Java Agent，这个特定的手段就是VirtualMachine的attach api，这个api其实是JVM进程之间的的沟通桥梁，底层通过socket进行通信，JVM A可以发送一些指令给JVM B，B收到指令之后，可以执行对应的逻辑，比如在命令行中经常使用的jstack、jcmd、jps等，很多都是基于这种机制实现的。
//
//作者：占小狼
//链接：https://www.jianshu.com/p/5bfe16c9ce4e
//来源：简书
//著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
    //使用attach api的也是一个独立的Java进程，下面是一个简单的实
        // 15186表示目标进程的PID
//        VirtualMachine vm = VirtualMachine.attach("15186");
//        try {
//            // 指定Java Agent的jar包路径
//            vm.loadAgent(".../agent.jar");
//        } finally {
//            vm.detach();
//        }
    }
}
