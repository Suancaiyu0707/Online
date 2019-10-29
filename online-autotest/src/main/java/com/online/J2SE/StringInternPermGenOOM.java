package com.online.J2SE;

import org.springframework.cglib.proxy.Enhancer;

import java.util.ArrayList;
import java.util.List;

/***
 * -XX:+PrintGCDetails -Xmn5m -Xms20m -Xmx20m -XX:PermSize10m -XX:MaxPermSize10m
 * Java HotSpot(TM) 64-Bit Server VM warning: ignoring option PermSize10m; support was removed in 8.0
 * Java HotSpot(TM) 64-Bit Server VM warning: ignoring option MaxPermSize10m; support was removed in 8.0
 * -XX:+PrintGCDetails -XX:PermSize10m -XX:MaxPermSize10m
 */
public class StringInternPermGenOOM {
    public static void main(String[] args) {
        int i =0 ;
        List<String> list = new ArrayList<>();
        while (true){
            list.add(("快点，我要回收内存"+i++).intern());
        }


    }
}
