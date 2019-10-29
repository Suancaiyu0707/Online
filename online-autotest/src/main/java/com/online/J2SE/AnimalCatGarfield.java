package com.online.J2SE;

import java.util.ArrayList;
import java.util.List;

public class AnimalCatGarfield {
    public static void main(String[] args) {
        List<Animal> animalLst = new ArrayList<Animal>();
        List<Cat> catList = new ArrayList<Cat>();
        List<Garfield> garfields = new ArrayList<Garfield>();

        animalLst.add(new Animal());
        catList.add(new Cat());
        garfields.add(new Garfield());
        //编译出错，只能赋值Cat或Cate子类
        //List<? extends Cat> extendsCatFromAnimal = animalLst;

        List<? super Cat> superCatFromAnimal = animalLst;

        List<? extends Cat> extendsCatFromCat = catList;
        List<? super Cat> superCatFromCat = catList;

        List<? extends Cat> extendsCatFromGarfield = garfields;
        //编译报错，只能赋值Cat或Cat父类
        //List<? super Cat> superCatFromGarfield = garfields;
        //下面三行都会编译报错，所有的<？ extends T>都无法进行add操作，因为 extendsCatFromCat 实际的类型肯定是Cat或Cat的子类，但是就是不确定是Cat的哪个子类
//        extendsCatFromCat.add(new Animal());
//        extendsCatFromCat.add(new Cat());
//        extendsCatFromCat.add(new Garfield());
        //下行编译出错，只能编译Cat或Cat子类的集合
       // superCatFromCat.add(new Animal());
        //<？ super Cat> 可以往里面添加元素，但是只能添加Cat或Cat的子类，因为superCatFromCat实际类型指向的肯定是Cat或cat的父类，所以Cat的子类也肯定是superCatFromCat实际类型的子类

        superCatFromCat.add(new Cat());
        superCatFromCat.add(new Garfield());
        //extendsCatFromCat里的类型肯定是Cat的子类或者Cat本身，但是无法确定是哪种具体子类，所以可以自动转换成Cat，但是不能转换成Garfield
        Object catExtends2 = extendsCatFromCat.get(0);
        Cat catExtends1 = extendsCatFromCat.get(0);
        //下面编译会出错，虽然Cat集合从Garfield赋值而来，但类型擦除后是不知道

        //Garfield garfieldl = extendsCatFromGarfield.get(0);


    }
}
