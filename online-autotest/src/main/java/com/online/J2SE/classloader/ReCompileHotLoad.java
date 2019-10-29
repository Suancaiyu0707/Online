package com.online.J2SE.classloader;

/***
 * 执行这个类，用来触发修改HotLoad后重新编译HotLoad类
 */
public class ReCompileHotLoad {
    public static void main(String[] args) {
        new HotLoad().operation();
    }
}