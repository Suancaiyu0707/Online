package com.online.SPI.impl;

import com.alibaba.dubbo.common.URL;
import com.online.SPI.AdaptiveProtocol;

public class AdaptiveProtocolImpl3 implements AdaptiveProtocol {
    public String hi(URL url, String s) {
        return "AdaptiveProtocolImpl3-hi";
    }

    public String hello(URL url, String s) {
        return "AdaptiveProtocolImpl3-hello";
    }
}
