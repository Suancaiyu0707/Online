package com.online.bio;

import com.online.network.util.CloseUtil;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/****
 * 利用java套接字实现一个BIO的网络程序
 * 我们发现，要处理多个客户端连接的时候，我们需要为每个新的客户端创建一个socket，并为每个socket创建一个thread，者有很多缺点
 *  1、任何时候，都可能有大量的线程处于休眠状态，只是为了阻塞等待输入和输出数据就绪
 *  2、为每一个客户端连接都创建一个线程，而通常线程的创建需要为调用栈分配内存，默认是64kb到1mb，这会大量浪费内存
 *  3、jvm在物理上可以支持非常大数量的线程，但是大量的上下文切换所带来的开销可不容忽视。
 *
 */
public class OioServer {
    public void serve(int port) throws IOException{
        final ServerSocket serverSocket = new ServerSocket(port);
        try{
            while(true){
                //如果没有连接过来，这边会一直阻塞

                final Socket clientSocket = serverSocket.accept();
                System.out.println("Accepted connection from "+clientSocket);
                //返回给客户端新的socket，并新启一个线程处理业务逻辑，而主线程继续监听新的连接
                //这样可保主线程不会因为socket的读写而阻塞
                new Thread(new Runnable() {
                    public void run() {
                        OutputStream os = null;
                        InputStream in = null;
                        BufferedReader reader = null;
                        try {
                           os = clientSocket.getOutputStream();
                           in = clientSocket.getInputStream();
                           reader = new BufferedReader(new InputStreamReader(in));
                           String request;
                           while((request=reader.readLine())!=null){
                               if("end".equals(request)){//判断全部接收完成，则跳出循环
                                   break;
                               }
                               System.out.println("receive msg="+request);
                               System.out.println();
                           }

                          // os.write("Hi!".getBytes("UTF-8"));
                           os.flush();
                           //clientSocket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }finally {
                            try{
                                CloseUtil.close(os);
                                CloseUtil.close(in);
                                CloseUtil.close(reader);
                                CloseUtil.close(clientSocket);
                            }catch(IOException ioe){

                            }
                        }

                    }
                }).start();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public static void main(String args[]) throws IOException {
        OioServer server = new OioServer();
        server.serve(8000);
    }


}
