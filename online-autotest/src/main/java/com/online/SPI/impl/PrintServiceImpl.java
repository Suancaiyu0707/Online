package com.online.SPI.impl;

import com.online.SPI.PrintService;

import java.util.ServiceLoader;

public class PrintServiceImpl implements PrintService {
    @Override
    public void printInfo() {
        System.out.println("hello world");
    }

}
