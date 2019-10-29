package com.online.SPI;

import com.alibaba.dubbo.common.extension.SPI;

@SPI("impl1")//spi的的value指定了默认的拓展点的实现类，和com.online.SPI.DubboSPIService中的配置匹配
public interface DubboSPIService {
    void printinfo();
}
