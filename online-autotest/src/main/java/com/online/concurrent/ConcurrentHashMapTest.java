package com.online.concurrent;

import java.util.concurrent.ConcurrentHashMap;

public class ConcurrentHashMapTest {
    public static void main(String[] args) {
        ConcurrentHashMap<String,String> map = new ConcurrentHashMap(16);
        map.put("a","a");
    }
}
