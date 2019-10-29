package com.online.leetcode;

import com.alibaba.dubbo.common.utils.StringUtils;

public class StackTest {

    public static boolean validateStr(String str){
        if(StringUtils.isEmpty(str)) return true;
        while(true){
            int len = str.trim().length();
            str =   str.replace("()","").replace("[]","").replace("{}","");
            if(str.length()==len){
                break;
            }
        }
       return StringUtils.isEmpty(str);
    }

    public static void main(String[] args) {
        System.out.println(validateStr("[]"));
    }
}
