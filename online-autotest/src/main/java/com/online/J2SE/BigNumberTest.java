package com.online.J2SE;

import java.math.BigDecimal;
import java.math.BigInteger;

public class BigNumberTest {
    /**
     * 填充原字符串
     * @param now 填充前的字符串
     * @param expectLength 目标长度
     * @param paddingchar 填充符号
     * @return
     */
    private static String lpad(String now,int expectLength,char paddingchar){

        if(now==null||now.length()>=expectLength){
            return now;
        }
        StringBuilder sb = new StringBuilder(expectLength);
        for(int i=0;i<now.length()-expectLength;i++){
            sb.append(paddingchar);
        }
        return sb.append(now).toString();
    }

    public static void main(String[] args) {
        BigDecimal bigDecimal = new BigDecimal("2719289831973819739173917319739173917391");
        System.out.println("数字的原始值是： "+bigDecimal);
        bigDecimal=bigDecimal.add(BigDecimal.TEN);

        System.out.println("添加10以后： "+bigDecimal);

        byte[] bytes = bigDecimal.toBigInteger().toByteArray();
        for(byte b : bytes){//一个byte是一个直接，也就是8位，所以跟11111111组合运算
            String bitString = lpad(Integer.toBinaryString(b&0xff),8,'0');
            System.out.println(bitString);
        }

        BigInteger bigInteger = new BigInteger(bytes);
        System.out.println("还原结果为： "+bigDecimal);
    }
}
