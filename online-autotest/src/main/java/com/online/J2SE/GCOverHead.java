package com.online.J2SE;

import java.util.ArrayList;
import java.util.List;

/***
 * 一直做FUll GC 但是就是不跑出OOM
 *
 * -XX:+PrintGCDetails -Xmn5m -Xms20m -Xmx20m
 * 堆内存：19.5M
 * 老年代：15M
 * 新生代：5M
 *      eden:4M
 *      from:0.5M
 *      to:0.5M
 */
public class GCOverHead {
    //这里先占用 old区域 超过12Mb的空间
    public final  static byte[] Default_bytes = new byte[12 * 1024 *1024];

    public static void main(String[] args) {
        List<byte[]> temp = new ArrayList<>();
        while (true){
            temp.add(new byte[1024*1024]);
            if (temp.size()>3){
                temp.clear();
            }
        }
    }
}
