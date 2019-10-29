package com.online.SPI;

import com.alibaba.dubbo.common.extension.ExtensionLoader;

/***
 * 1、Java spi记载失败，可能会因为各种原因导致异常信息被吞掉，导致问题追踪比较困难。DUBBO SPI在加载失败的时候，会先抛出真实异常并打印日志。而且扩展点在被动加载的时候，即使失败了也不影响整个框架的使用
 * 2、DUBBO API自己实现了AOP和IOC机制，一个拓展点可以通过setter方法直接注入到其他拓展方法。另外dubbo支持包装拓展类，推荐把通用的抽象逻辑放到包装类中，用于实现拓展点的aop特性
 */
public class TestDubboExtensionLoader {
    public static void main(String[] args) {
        /***
         * 用ExtensionLoader加载指定的拓展点，并获得拓展点的默认实现类
         * 默认实现类这里是通过spi注解的value来指定
    */
        DubboSPIService spiService = ExtensionLoader.getExtensionLoader(DubboSPIService.class).getDefaultExtension();
        spiService.printinfo();
    }
}
