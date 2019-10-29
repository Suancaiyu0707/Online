package com.online.J2SE;

public class P<T> {

    private T item;
    public P(T t){item=t;}
    public void set(T t){item=t;}
    public T get(){return item;}

}
