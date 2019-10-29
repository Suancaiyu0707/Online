package com.online.leetcode;


import java.util.HashMap;
import java.util.Map;

/***
 * 两数之和
 * @author zhifang.xu
 */
public class TwoSum {

    static int[] sum(int[] source,int target){
        if(source==null||source.length<2){
            return new int[]{};
        }
        Map<Integer,Integer> map = new HashMap<Integer,Integer>();
        for(int i=0;i<source.length;i++){
            map.put(source[i],i);
        }
        for(int j=0;j<source.length;j++){
            int findval = target-source[j];
            if(map.get(findval)!=null&&map.get(findval)!=j){
                return new int[]{j,map.get(findval)};
            }
        }
        return new int[]{};
    }


    public static void main(String[] args) {
        int[] source = {1,3,4,6,9};
        int [] result = sum(source,10);
        for(int i:result){
            System.out.println(i);
        }


    }
}
