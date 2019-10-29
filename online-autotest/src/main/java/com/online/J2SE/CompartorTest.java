package com.online.J2SE;

import java.util.Comparator;

/***
 * CompartorTest本身跟被比较的对象是对立的，所以修改比较算法的时候，对比较对象无影响
 */
public class CompartorTest implements Comparator {
    @Override
    public int compare(Object o1, Object o2) {
        return 0;
    }
}
