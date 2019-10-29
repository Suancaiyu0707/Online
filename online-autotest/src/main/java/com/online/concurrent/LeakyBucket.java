package com.online.concurrent;

import java.util.Random;

/***
 * 漏桶算法
 */
public class LeakyBucket {
    double rate; // leak rate in calls/s
    double burst; // bucket size in calls
    long refreshTime; // time for last water refresh
    double water; // water count at refreshTime

    void refreshWater() {
        long now = System.currentTimeMillis();
//水随着时间流逝,不断流走,最多就流干到0.
        water = Math.max(0, water - (now - refreshTime) * rate);
        refreshTime = now;
    }

    boolean permissionGranted() {
        refreshWater();
        if (water < burst) { // 水桶还没满,继续加1
            water++;
            return true;
        } else {
            return false;
        }

    }
    
}
