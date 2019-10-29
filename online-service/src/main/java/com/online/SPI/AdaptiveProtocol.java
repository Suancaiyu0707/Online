package com.online.SPI;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.Adaptive;
import com.alibaba.dubbo.common.extension.SPI;

@SPI("impl1")//如果下面的Adaptive都不满足，则默认的用impl1拓展实现类
public interface AdaptiveProtocol {
    @Adaptive({"key1", "protocol"})//先key1 后protocol
    String hi(URL url, String s);

    @Adaptive({"protocol", "key2"})//先protocol 后key2
    String hello(URL url, String s);
}
