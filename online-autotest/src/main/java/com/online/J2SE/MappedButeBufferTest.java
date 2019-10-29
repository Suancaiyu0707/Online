package com.online.J2SE;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.SocketChannel;

public class MappedButeBufferTest {
    public static void copyFileMappedByteBuffer(String srcFile,String destFile) throws Exception {
        /***
         * RandomAccessFile是Java输入/输出流体系中功能最丰富的文件内容访问类，既可以读取文件内容，也可以向文件输出数据。与普通的输入/输出流不同的是，RandomAccessFile支持跳到文件任意位置读写数据，
         * RandomAccessFile对象包含一个记录指针，用以标识当前读写处的位置，当程序创建一个新的RandomAccessFile对象时，该对象的文件记录指针对于文件头（也就是0处），
         * 当读写n个字节后，文件记录指针将会向后移动n个字节。除此之外，RandomAccessFile可以自由移动该记录指针
         */
        FileChannel infileChannel = new RandomAccessFile(srcFile,"r").getChannel();//以只读方式打开指定文件。如果试图对该RandomAccessFile指定的文件执行写入方法则会抛出IOException
        FileChannel outfileChannel = new RandomAccessFile(destFile,"rw").getChannel();//以读取、写入方式打开指定文件。如果该文件不存在，则尝试创建文件
//        FileLock lock = infileChannel.tryLock();
//        lock.release();
        //获得文件大小
        long fileSize = infileChannel.size();
        //用来统计已读取的文件大小
        long position =0;
        while(position<fileSize){//如果文件还没读完，则继续读取
            //比较剩余的文件大小和最多读取的上限
            long copyFileSize = Math.min(fileSize-position,4*1024);
            //创建一个输入文件的共享的内存映射
            //通过map在磁盘上对应的位置直接分配一块共享的内存映射空间，本质也是一块直接内存，它是直接映射到磁盘上的，并不是没有缓冲区，只是这个缓冲区是由os来管理的，此时若对这个共享内存进行写入的话，就直接写到磁盘上
            MappedByteBuffer mappedByteBuffer = outfileChannel.map(FileChannel.MapMode.READ_WRITE,position,copyFileSize);
            //将字节写入到这个共享的内存映射中，也就是直接写入到输入文件里
            infileChannel.read(mappedByteBuffer);
            //累加已读的字节流
            position+=mappedByteBuffer.position();
        }

    }
}
