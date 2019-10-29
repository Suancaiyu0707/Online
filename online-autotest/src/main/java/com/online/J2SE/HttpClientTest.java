package com.online.J2SE;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpClientTest {
    public static void main(String[] args) throws IOException {
        // 1. 得到访问地址的URL
        URL url = new URL("http://localhost:8080/lineage/fetch.do?dbname=ods&table=tabname");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    }
}
