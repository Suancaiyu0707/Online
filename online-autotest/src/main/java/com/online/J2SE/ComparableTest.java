package com.online.J2SE;

/***
 * 如果要改变比较算法，只能修改目标比较对象里的实现方法，有侵入型
 */
public class ComparableTest implements Comparable{
    @Override
    public int compareTo(Object o) {
        return 0;
    }
}
