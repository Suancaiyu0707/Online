import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.online.common.util.ProtostuffUtil;
import org.wit.ff.testmodel.*;

import java.io.*;
import java.util.Arrays;

public class TestSerializable {
    public static void main(String[] args) throws IOException, ClassNotFoundException {

        Student student = new Student();
        student.setName("luocheng");
        student.setAge(24);
        student.setStudentNo("2017080222");
        student.setSchoolName("yangzi");

        Student1 student1 = new Student1();

        student1.setName("luocheng");
        student1.setAge(24);
        student1.setStudentNo("2017080222");
        student1.setSchoolName("yangzi");

        Student2 student2 = new Student2();

        student2.setName("luocheng");
        student2.setAge(24);
        student2.setStudentNo("2017080222");
        student2.setSchoolName("yangzi");

        Student3 student3 = new Student3();

        student3.setName("luocheng");
        student3.setAge(24);
        student3.setStudentNo("2017080222");
        student3.setSchoolName("yangzi");


        //使用开源的Protostuff序列化和反序列化
        byte[] serializerResult = ProtostuffUtil.serializer(student);

        System.out.println("使用开源的Protostuff序列化和反序列化 size:" + serializerResult.length);

        StudentD deSerializerResult = ProtostuffUtil.deserializer(serializerResult,StudentD.class);
        System.out.println("使用开源的Protostuff序列化Result:" + Arrays.toString(serializerResult));
        System.out.println("使用开源的Protostuff反序列化Result:" + deSerializerResult.toString());

        System.out.println("----------------------------------------------------");
        //实现Serializable接口序列化和反序列化
        int size = ProtostuffUtil.calcSize(student1);
        System.out.println("实现Serializable接口序列化和反序列化 size:" + size);

        System.out.println("实现Serializable接口序列化和反序列化Result:" + student1.toString());
        System.out.println("----------------------------------------------------");
        //fastjson序列化
        String studentJson = JSON.toJSONString(student3);
        int size2 = studentJson.length();
        //反序列化
        JSONObject ins1 = JSON.parseObject(studentJson, JSONObject.class);
        System.out.println("fastjson序列化 size:"+size2);
        System.out.println("fastjson序列化Result:" + student2);
        System.out.println("fastjson反序列化Result:" + ins1.toJSONString());

        //序列化到本地
        ObjectOutputStream out=new ObjectOutputStream(new FileOutputStream("E:/Student2.txt"));
        out.writeObject(student2);
        out.close();

        //反序列化
        ObjectInputStream in=new ObjectInputStream(new FileInputStream("E:/Student2.txt"));
        Student2 student4=(Student2)in.readObject();
        in.close();
        int size3 = ProtostuffUtil.calcSize(student2);
        System.out.println("实现Serializable接口序列化和反序列化 size:" + size3);

        System.out.println("实现Serializable接口序列化和反序列化Result:" + student4.toString());
        System.out.println("----------------------------------------------------");
    }
}
