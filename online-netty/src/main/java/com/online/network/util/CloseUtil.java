package com.online.network.util;

import java.io.Closeable;
import java.io.IOException;

/***
 * 关闭文件句炳的工具
 */
public class CloseUtil {
    public static void close(Closeable close) throws IOException {
        if(close!=null){
            close.close();
        }
    }
}
