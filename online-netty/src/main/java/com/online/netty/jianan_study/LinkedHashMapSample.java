package com.online.netty.jianan_study;

import java.util.LinkedHashMap;
import java.util.Map;

public class LinkedHashMapSample {
    public static void main(String[] args) {
        LinkedHashMap<String, String> accessOrderedMap = new LinkedHashMap<String, String>(16, 0.75F, true){
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, String> eldest) { // 实现自定义删除策略，否则行为就和普遍 Map 没有区别
                return size() > 3;
            }
        };
        accessOrderedMap.put("Project1", "Valhalla");
        accessOrderedMap.put("Project2", "Panama");
        accessOrderedMap.put("Project3", "Loom");
        for(Map.Entry entry :accessOrderedMap.entrySet()){
            System.out.println(entry.getKey() +":" + entry.getValue());
        }

        // 模拟访问
        accessOrderedMap.get("Project2");
        accessOrderedMap.get("Project2");
        accessOrderedMap.get("Project3");
        System.out.println("Iterate over should be not affected:");
        for(Map.Entry entry :accessOrderedMap.entrySet()){
            System.out.println(entry.getKey() +":" + entry.getValue());
        }
        // 触发删除
        accessOrderedMap.put("Project4", "Mission Control");
        System.out.println("Oldest entry should be removed:");
        for(Map.Entry entry :accessOrderedMap.entrySet()){
            System.out.println(entry.getKey() +":" + entry.getValue());
        }
    }
}
