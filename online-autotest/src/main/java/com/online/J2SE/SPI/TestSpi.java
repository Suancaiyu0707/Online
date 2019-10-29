package com.online.J2SE.SPI;

import java.util.ServiceLoader;

public class TestSpi {
    public static void main(String[] args) {
        //通过serviceloder获取所有的SpiService实现
        ServiceLoader<SpiService> services=ServiceLoader.load(SpiService.class);
        for(SpiService spiService:services){
            System.out.println(spiService.getClass().getName());
            spiService.sayHello();
        }
    }
}
