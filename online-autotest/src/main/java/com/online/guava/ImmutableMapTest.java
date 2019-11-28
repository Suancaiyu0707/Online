package com.online.guava;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: zhifang.xu
 * Date: 2019/11/16
 * Time: 10:03 AM
 * Description: No Description
 */
public class ImmutableMapTest {
    public static void main( String[] args ) {
        /***
         * Guava提供的ImmutableMap是一个支持多线程环境下面的安全的Map，
         * 同时效率也是很高的Key-Value集合，为什么他就是安全的呢
         */
        ImmutableMap.Builder<String, Object> request = ImmutableMap.builder();
        request.put("one","1");
        request.put("two","2");
        request.put("three","3");
        Map<String, Object> map = request.build();


        Map<Integer, String> INTEGER_STRING_MAP =
                new ImmutableMap.Builder<Integer, String>().

                        put(30, "IP地址或地址段").
                        put(31, "端口号或范围").
                        put(32, "IP地址或地址段").
                        put(33, "端口号或范围").
                        put(34, "代码值").
                        put(38, "探针名称").
                        put(39, "网络协议号(protocol)").
                        put(40, "ipv6源IP(ipv6_src_addr)").
                        put(41, "ipv6目标IP(ipv6_dst_addr)").
                        put(42, "网络协议名称(protocol_map)").
                        put(43, "输入接口snmp(input_snmp)")

                        .build();
    }
}
