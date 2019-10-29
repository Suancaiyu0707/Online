package org.wit.ff.testmodel.ch1;

import java.io.Serializable;

public class SerializableUserA implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    private int age;
    private  String firstName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
}
