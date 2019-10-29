package com.online.common.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;

public class FileTypeUtil {
    // 缓存文件头信息-文件头信息
    private static final HashMap<String, String> fileTypeMap = new HashMap<String, String>();
    static {
        // images
        fileTypeMap.put("FFD8FF", "jpg");
        fileTypeMap.put("89504E47", "png");
        fileTypeMap.put("47494638", "gif");
        fileTypeMap.put("49492A00", "tif");
        fileTypeMap.put("424D", "bmp");
        //
        fileTypeMap.put("3C3F786D6C", "xml");
        fileTypeMap.put("68746D6C3E", "html");
        fileTypeMap.put("44656C69766572792D646174653A", "eml"); // 邮件
        fileTypeMap.put("D0CF11E0", "doc");
        fileTypeMap.put("D0CF11E0", "xls");//excel2003
        fileTypeMap.put("504B0304", "xlsx");//excel2007
        fileTypeMap.put("255044462D312E", "pdf");
        fileTypeMap.put("504B0304", "docx");

        fileTypeMap.put("52617221", "rar");
        fileTypeMap.put("57415645", "wav");
        fileTypeMap.put("41564920", "avi");
        fileTypeMap.put("2E524D46", "rm");
        fileTypeMap.put("000001BA", "mpg");
        fileTypeMap.put("000001B3", "mpg");
        fileTypeMap.put("6D6F6F76", "mov");
        fileTypeMap.put("3026B2758E66CF11", "asf");
        fileTypeMap.put("4D546864", "mid");
        fileTypeMap.put("1F8B08", "gz");
    }

    /**
     * @param filePath 文件路径
     * @return 文件头信息
     * @author zhifang.xu
     * <p>
     * 方法描述：根据文件路径获取文件头信息
     */
    public static String getFileType(String filePath) {
        return fileTypeMap.get(getFileHeader(filePath));
    }

    /**
     * @param filePath 文件路径
     * @return 文件头信息
     * @author zhifang.xu
     * <p>
     * 方法描述：根据文件路径获取文件头信息
     */
    public static String getFileHeader(String filePath) {
        FileInputStream is = null;
        String value = null;
        try {
            is = new FileInputStream(filePath);
            byte[] b = new byte[4];
            /*
             * int read() 从此输入流中读取一个数据字节。int read(byte[] b) 从此输入流中将最多 b.length
             * 个字节的数据读入一个 byte 数组中。 int read(byte[] b, int off, int len)
             * 从此输入流中将最多 len 个字节的数据读入一个 byte 数组中。
             */
            is.read(b, 0, b.length);
            value = bytesToHexString(b);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return value;
    }

    /**
     * @param src 要读取文件头信息的文件的byte数组
     * @return 文件头信息
     * @author zhifang.xu
     * <p>
     * 方法描述：将要读取文件头信息的文件的byte数组转换成string类型表示
     */
    private static String bytesToHexString(byte[] src) {
        StringBuilder builder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        String hv;
        for (byte aSrc : src) {
            // 以十六进制（基数 16）无符号整数形式返回一个整数参数的字符串表示形式，并转换为大写
            hv = Integer.toHexString(aSrc & 0xFF).toUpperCase();
            if (hv.length() < 2) {
                builder.append(0);
            }
            builder.append(hv);
        }
//		System.out.println(builder.toString());
        return builder.toString();
    }

    /**
     * @param args
     * @throws Exception
     * @author zhifang.xu
     * <p>
     * 方法描述：测试
     */
    public static void main(String[] args) throws Exception {
        final String fileType = getFileType("/Users/hb/Documents/调度平台.docx");
        System.out.println(fileType);
        System.out.println(getFileHeader("E/Users/hb/Documents/调度平台.docx"));
    }
    
}
