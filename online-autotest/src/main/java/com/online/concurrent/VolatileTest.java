package com.online.concurrent;

/***
 * 计算机中正常的数据途经:磁盘-->内存(所有的处理器共享)-->高速缓存(每个处理器都有自己的高速缓存)-->处理器(CPU)
 * 1、volatile 不会引起线程上下文的切换和调度
 * 2、volatile 修饰的变量，在转换成汇编指令的时候,会生成一个Lock前缀指令，Lock前缀指令导致在执行指令期间，声言处理器的LOCK#信号（注意Lock前缀指令和LOCK#信号是两个概念）。
 *      LOCK#信号确保在声言该信号期间，处理器可以独占共享内存，所以说 Lock前缀指令 会使紧跟在其后面的指令变成 原子操作（atomic instruction）。
 *      暂时的锁一下总线(最近的处理器里，LOCK#信号一般不锁总线，而是锁缓存，因为锁总线的开销比较大)，指令执行完了，总线就解锁了！！该指令在多核处理器下会引发两件事情：
 *      1）将当前处理器缓存行的数据写回到系统内存。
 *      2）这个写回内存的操作会使在其它CPU里缓存了该内存地址的数据无效。
 *
 * 3、volatile的两条实现原则
 *      1）Lock前缀指令会引起处理器缓存回写到内存。
 *          Lock前缀指令导致在执行指令期间，声言处理器的LOCK#信号（注意Lock前缀指令和LOCK#信号是两个概念）。LOCK#信号确保在声言该信号期间，处理器可以独占共享内存。
 *          但是最近的处理器里，LOCK#信号一般不锁总线，而是锁缓存，因为锁总线的开销比较大。
 *          在Intel486和Pentium处理器，在锁操作时，总是在总线上声言LOCK#信号。但是在P6和目前的处理器中，如果访问的内存区域已经缓存在处理器内部，则不会声言LOCK#信号。
 *          相反，它会锁定这块内存区域的缓存并写到内存，并使用缓存一致性机制来确保修改的原子性，此操作被称为"缓存锁定"，缓存一致性会组织同时修改两个以上处理器缓存的内存区域数据。
 *
 *      2）一个处理器的缓存(比如高速缓存)回写到内存,会导致其它处理的缓存无效。
 *          IA-32处理器和Intel64处理器使用MESI(修改modify、独占engross、共享share、无效Invalid)控制协议去维护内部缓存和其它处理器缓存的一致性。
 *              IA-32和Intel64处理器采用的是嗅探技术：在多核处理器系统中，处理器使用嗅探技术保证处理器本身的的内部缓存(高速缓存)、系统内存和其它的处理器的内部缓存的数据在总线上保持一致。
 *              Pentium和P6 family处理器通过嗅探一个处理器来检测其他处理器打算写内存地址，如果这个内存地址当前处于共享状态，那么正在嗅探的处理器将使它本身的缓存行无效，在下次访问相同内存地址时，强制执行缓存行填充。
 *
 */
public class VolatileTest extends Thread {

    public static /***volatile**/  boolean flag = false;
    int i = 0;

    public void run() {
        while (!VolatileTest.flag) {
            i++;
        }
        System.out.println("flag="+VolatileTest.flag+"，i="+i);
    }

    public static void main(String[] args) throws Exception {
        VolatileTest vt = new VolatileTest();
        vt.start();
        Thread.sleep(2000);
        VolatileTest.flag = true;
        //System.out.println("stop:" + vt.i);
    }
}
