package com.online.J2SE;

import java.util.ArrayList;
import java.util.List;

public class Cat extends Animal {
    public static void main(String[] args) {
        Cat b1 = new Cat();
        Cat b2= new Cat();
        List<Cat> list = new ArrayList<Cat>();
        list.sort(new CompartorTest ());
        ComparableTest t1 = new ComparableTest();
        ComparableTest t2 = new ComparableTest();
        t1.compareTo(t2);
    }
}
