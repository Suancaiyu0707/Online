package com.online.bio;

import java.io.*;

import java.net.Socket;

public class OioClient {
    public static final int port = 8000;
    public static final String host = "127.0.0.1";
    public static void main(String[] args) throws IOException {
        final Socket socket = new Socket(host,port);
        new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("client start success!");
                while(true){
                    String message = "hello,nice to meet you,my name is xuzf !";
                    System.out.println("客户端发送数据："+message);
                    try {
                        OutputStream os =socket.getOutputStream();
                        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
                        bw.write(message);
                        bw.flush();
                    } catch (IOException e) {
                        System.out.println("写数据出错！");
                    }
                    sleep();
                }
            }
        }).start();
    }

    public static void sleep(){
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
