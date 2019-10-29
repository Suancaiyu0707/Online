package com.online.netty.jianan_study;

public class ParamTest {

    public static void main(String[] args) {
        /*
         * 演示一：一个方法不能修改一个基本数据类型的参数（即数值型和布尔型）
         */
        System.out.println("Testing tripleValue:");
        double percent = 10;
        System.out.println("Before: percent="+percent);
        tripleValue(percent);//在tripleValue方法里的percent是复制出来的一份基本类型，所以在tripleValue里修改percent，并不回影响这边的percent
        System.out.println("After: percent="+percent);

        /*
         * 演示二：一个方法可以改变一个对象参数的状态
         */
        System.out.println("\nTesting tripleSalary:");
        Employee harry = new Employee("Harry", 50000);
        System.out.println("Before: salary="+harry.getSalary());
        tripleSalary(harry);//在tripleValue方法里的harry是复制出来的一份对象的引用，复制出来的对象引用和这边的harry是指向同一个对象，
        System.out.println("After: salary="+harry.getSalary());

        /*
         * 演示三：一个方法不能让对象参数引用一个新的对象
         */
        System.out.println("\nTesting swap:");
        //java栈内存中的对像引用a和b指向了堆中两个实际对象new Employee("Alice", 7000)和new Employee("Bob", 6000)
        Employee a = new Employee("Alice", 7000);
        Employee b = new Employee("Bob", 6000);
        System.out.println("Before: a="+a.getName());
        System.out.println("Before: b="+b.getName());
        swap(a, b);//整个过程对象引用a和b都没有发生改变
        System.out.println("After: a="+a.getName());
        System.out.println("After: b="+b.getName());
    }
    public static void tripleValue(double x) {
        //基础类型都是不可变的，方法传递的时候，是在java栈内存里复制出一份变量，所以这里的x和main方法里传过来的那个参数是两份不同的变量
        //所以这边就算修改了x,也不回改变main的值
        x = 3*x;
        System.out.println("End of method: x="+x);
    }
    //对于对象传参，它其实是复制的引用，就是说这里的x和main方法里传过来的那个引用不是同一个，但是它们指向的对象是堆内存里的同一个对象
    public static void tripleSalary(Employee x) {
        x.raiseSalary(200);
        System.out.println("End of method: salary="+x.getSalary());
    }

    public static void swap(Employee x, Employee y) {
        //java栈中的对象temp引用和对象x引用同时指向new Employee("Alice", 7000)
        Employee temp = x;
        //java栈中的对象x引用重新指向new Employee("Bob", 6000)
        x = y;
        //java栈中的对象y引用重新指向new Employee("Alice", 7000)
        y = temp;
        System.out.println("End of method: x="+x.getName());
        System.out.println("End of method: y="+y.getName());
    }
}


class Employee {
    private String name;
    private double salary;

    public Employee(String n, double s) {
        name = n;
        salary = s;
    }

    public String getName() {
        return name;
    }

    public double getSalary() {
        return salary;
    }

    public void raiseSalary(double byPercent) {
        double raise = salary * byPercent / 100;
        salary += raise;
    }
}
