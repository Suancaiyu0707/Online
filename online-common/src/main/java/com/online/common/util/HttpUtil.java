package com.online.common.util;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

public class HttpUtil {

    public static void main(String[] args) {
        for(int i=0;i<5;i++){
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        createConn();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        }
    }

    public static void createConn() throws IOException, InterruptedException {
        URL url =null;

        try {
            url = new URL("http://127.0.0.1");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.connect();

            TimeUnit.MINUTES.sleep(1);
            conn.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
}
