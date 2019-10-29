package com.online.J2SE.SPI;

public class SpiServiceImpl implements SpiService{
    @Override
    public void sayHello() {
        System.out.println("sayhello SpiServiceImpl");
    }
}
