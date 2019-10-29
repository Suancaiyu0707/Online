package com.online.J2SE;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.*;

public class A {
    public static void main(String[] args) {
        List<String > list = Lists.newArrayList();

        Map<String,List<String>> map =new HashMap<>();
        for(String s : list){
            //将每个字符串按照字符排序
            char[] chars = s.toCharArray();

            Arrays.sort(chars);
            //把字符转换成字符串比较
            String news= new String(chars);
            if(map.get(news)!=null){
                map.get(news).add(s);
            }else{
                List <String>l = new ArrayList();
                l.add(s);
                map.put(news,l);
            }
        }
    }
}
