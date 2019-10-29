package com.online.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/***
 * 这是非阻塞的NIO网络编程，利用java nio来实现
 * 原理是：底层采用了NIO提供的多路复用选择器Selector。
 *  java.nio.channels.Selector底层采用了事件通知API以确定一组非阻塞套接字哪些已经就绪能够进行I/O操作
 *  1、多路复用选择Selector可以同时处理,因此减少了内存管理和上下文切换所带来的开销
 *  2、当没有I/O操作处理的时候，selector就会阻塞，减少cpu的浪费
 *
 *  缺点：编写麻烦
 */
public class NioServer {

    private  ServerSocketChannel serverSocketChannel=null;
    private  ServerSocket serverSocket = null;
    //通过定义一个多路复用选择器selector来处理请求事件
    private  Selector selector;



    public void initServer(String host,int port) throws IOException {
        this.serverSocketChannel = ServerSocketChannel.open();
        this.serverSocketChannel.configureBlocking(false);
        this.serverSocket = serverSocketChannel.socket();
        InetSocketAddress address =new InetSocketAddress(port);
        this.serverSocket.bind(address);
        this.selector = Selector.open();
        this.serverSocketChannel.register(this.selector,SelectionKey.OP_ACCEPT);
    }
    public  void serve() throws IOException{
        final ByteBuffer msg = ByteBuffer.wrap("Hi!\r\n".getBytes());
        for(;;){
            int count = this.selector.select();
            Set<SelectionKey> selectKeys = this.selector.selectedKeys();
            Iterator<SelectionKey>iterator=selectKeys.iterator();
            while (iterator.hasNext()){
                SelectionKey key = iterator.next();
                iterator.remove();
                try{
                    if(key.isAcceptable()){
                        ServerSocketChannel server = (ServerSocketChannel) key.channel();
                        SocketChannel client = server.accept();
                        client.configureBlocking(false);
                        client.register(this.selector,SelectionKey.OP_WRITE|SelectionKey.OP_READ,msg.duplicate());
                        System.out.println("Accepted  connection from "+client);
                    }
                    if(key.isWritable()){
                        SocketChannel client = (SocketChannel) key.channel();
                        ByteBuffer buffer = (ByteBuffer) key.attachment();
                        while(buffer.hasRemaining()){
                            if(client.write(buffer)==0){
                                break;
                            }
                        }
                    }
                    if(key.isReadable()){
                        SocketChannel channel = (SocketChannel) key.channel();
                        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);//分配的堆内存
                        String content = "";
                        try {
                            int readBytes = channel.read(byteBuffer);
                            if (readBytes > 0) {
                                byteBuffer.flip(); //为write()准备
                                byte[] bytes = new byte[byteBuffer.remaining()];
                                byteBuffer.get(bytes);
                                content+=new String(bytes);
                                System.out.println(content);
                                //回应客户端
                                doWrite(channel);
                            }
                            // 写完就把状态关注去掉，否则会一直触发写事件(改变自身关注事件)
                            key.interestOps(SelectionKey.OP_READ);
                        } catch (IOException i) {
                            //如果捕获到该SelectionKey对应的Channel时出现了异常,即表明该Channel对于的Client出现了问题
                            //所以从Selector中取消该SelectionKey的注册
                            key.cancel();
                            if (key.channel() != null) {
                                key.channel().close();
                            }
                        }
                    }
                }catch (IOException ex){
                    key.cancel();
                    key.channel().close();
                }
            }
        }
    }

    public static void main(String args[]) throws IOException {
        NioServer server = new NioServer();
        server.initServer("127.0.0.1",8080);
        server.serve();
        /***
        List<String>l = new ArrayList();
        l.add("1");
        l.add("2");

        Iterator<String>iterator = l.iterator();
        while (iterator.hasNext()){//判断当前游标是否是小于列表数组的长度
           // String key = iterator.next();//获取当前游标位置的列表元素，通常游标会一只是0
            iterator.remove();//移除当前游标位置的元素，并把游标重置为0。所以移除的是当前遍历的元素

        } */
    }

    private  static void doWrite(SocketChannel sc) throws IOException{
        byte[] req ="服务器已接受".getBytes();
        ByteBuffer byteBuffer = ByteBuffer.allocate(req.length);
        byteBuffer.put(req);
        byteBuffer.flip();
        sc.write(byteBuffer);
        if(!byteBuffer.hasRemaining()){
            System.out.println("Send 2 Service successed");
        }
    }
}
