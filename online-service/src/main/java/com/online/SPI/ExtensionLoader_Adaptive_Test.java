package com.online.SPI;

import com.alibaba.dubbo.common.URL;
import com.alibaba.dubbo.common.extension.ExtensionLoader;

import java.util.HashMap;
import java.util.Map;

public class ExtensionLoader_Adaptive_Test {
    public void test_getAdaptiveExtension_protocolKey() throws Exception {
        AdaptiveProtocol ext = ExtensionLoader.getExtensionLoader(AdaptiveProtocol.class).getAdaptiveExtension();

        {
            String hi = ext.hi(URL.valueOf("1.2.3.4:20880"), "s");
            System.out.println(hi);//默认值

            Map<String, String> map = new HashMap<String, String>();
            URL url = new URL("impl3", "1.2.3.4", 1010, "path1", map);

            hi = ext.hi(url, "s");
            System.out.println(hi);// 由于没有key1，这个时候通过 protocol来实例化拓展类

            url = url.addParameter("key1", "impl2");
            hi = ext.hi(url, "s");
            System.out.println(hi); //由于有key1, 这个时候通过 key1来实例化拓展类
        }

        {

            Map<String, String> map = new HashMap<String, String>();
            URL url = new URL(null, "1.2.3.4", 1010, "path1", map);
            String hello = ext.hello(url, "s");
            System.out.println(hello); // default value

            url = url.addParameter("key2", "impl2"); //由于没有 protocol，这个时候通过key2来实例化拓展类
            hello = ext.hello(url, "s");
            System.out.println(hello);

            url = url.setProtocol("impl3"); // 由于有 protocol，这个时候通过protocol来实例化拓展类
            hello = ext.hello(url, "d");
            System.out.println(hello);
        }
    }

}
