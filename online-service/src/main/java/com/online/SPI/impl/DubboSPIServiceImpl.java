package com.online.SPI.impl;

import com.online.SPI.DubboSPIService;

public class DubboSPIServiceImpl implements DubboSPIService {
    @Override
    public void printinfo() {
        System.out.println("print com.online.SPI.impl.DubboSPIServiceImpl");
    }
}
