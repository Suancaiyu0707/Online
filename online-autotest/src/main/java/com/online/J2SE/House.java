package com.online.J2SE;

public class House {
    private static final Integer DOOR_NUMBER = 2000;

    public House() {
        super();
    }

    @Override
    public String toString(){
       return "hello";
    }
    public Door[] doors= new Door[DOOR_NUMBER] ;
    class Door {}


}
