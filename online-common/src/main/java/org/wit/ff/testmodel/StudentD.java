package org.wit.ff.testmodel;


import io.protostuff.Tag;

public class StudentD {

    private int age;
    private String studentNo;
    private String name;

    private String schoolName;

    // 关于@Tag,要么所有属性都有@Tag注解,要么都没有,不能一个类中只有部分属性有@Tag注解


//    public String getDname() {
//        return dname;
//    }
//
//    public void setDname(String dname) {
//        this.dname = dname;
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStudentNo() {
        return studentNo;
    }

    public void setStudentNo(String studentNo) {
        this.studentNo = studentNo;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", studentNo='" + studentNo + '\'' +
                ", age=" + age +
                ", schoolName='" + schoolName + '\'' +
                '}';
    }
}