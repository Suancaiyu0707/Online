package com.online.J2SE;

public class SwitchTest {
    public static void main(String[] args) {
        Integer a =10;
        switch (a){
            case 2://case部分是不能写Integer对象，这边a会被自动拆箱进行比较
                System.out.println("=====2======");
                break;
            case 10:
                System.out.println("=====10======");
                break;

        }
        String b ="hello";
        switch (b){//switch一个字符串的话，会被编译成if else
            case "hello":
                System.out.println("=====hello======");
                break;
            case "world":
                System.out.println("=====world======");
                break;

        }
        /**会被编译成
         *
         *      String b = "hello";
         *         byte var4 = -1;
         *         switch(b.hashCode()) {
         *         case 99162322:
         *             if (b.equals("hello")) {
         *                 var4 = 0;
         *             }
         *             break;
         *         case 113318802:
         *             if (b.equals("world")) {
         *                 var4 = 1;
         *             }
         *         }
         *
         *         switch(var4) {
         *         case 0:
         *             System.out.println("=====hello======");
         *             break;
         *         case 1:
         *             System.out.println("=====world======");
         *         }
         */

    }
}
