package com.online.SPI.impl;

import com.online.SPI.PrintService;

import java.util.ServiceLoader;

public class Test {

    public static void main(String[] args) {
        ServiceLoader<PrintService> serviceServiceLoader =ServiceLoader.load(PrintService.class);

        for(PrintService printService:serviceServiceLoader){
            printService.printInfo();
        }
    }
}
