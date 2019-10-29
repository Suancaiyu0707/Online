package com.online.concurrent;

/***
 *在jdk1.6之前，synchronized 还是一个重量级锁，但是1.6之后，java se对synchronized做了一些优化，主要是包括偏向锁和轻量级锁。
 * synchronized 实现同步锁主要有3种形式：
 *  （1）对于普通同步方法(synchObjectMethodA)，锁是当前实例对象.
 *  （2）对于静态同步方法(synchClassMethodA)，锁是当前类的Class对象.
 *  （3）对于同步方法块(synchMethodBody)，锁是括号里配置的对象：obj.
 *
 * 2、如果锁是在加代码块里，那么编译后，它会在同步代码块的开始位置加上 monitorenter ,并在同步代码库结束位置加上 monitorexit;
 *    如果锁是加上对象上的话，那么线程在获得该锁的时候是通过获得跟对象关联的 monitor 的所有权。任何一个对象都会有一个与之关联的monitor。
 *
 * 3、在java中，锁一共有4种状态：无锁、偏向锁、轻量级锁和重量级锁，这几个状态会随着锁的竞争而逐渐升级。锁可以升级，但是不能降级，所以意味着偏向锁升级为轻量级锁后不能降级为偏向锁。这种锁可以升级但是不能降级的策略，是为了提高获得锁和释放锁的性能。
 */
public class SynchronizedTest{

    private static Object obj = new Object();

    private String name ;

    public SynchronizedTest(String name) {
        this.name = name;
    }

    public static void main(String[] args) throws InterruptedException {
        //testClassLock();
        //testObjectLock();
        testContentLock();
    }

    /***
     * 在Class对象上加锁
     * @throws InterruptedException
     */
    public static void testClassLock() throws InterruptedException {
        SynchronizedTest test_A = new SynchronizedTest("test_A");
        SynchronizedTest test_B = new SynchronizedTest("test_B");

        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    synchClassMethodA(test_A);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"thread_1");
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    synchClassMethodA(test_A);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"thread_2");

        Thread thread3 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    synchClassMethodA(test_B);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"thread_3");

        thread1.start();
        Thread.sleep(100);
        thread2.start();
        Thread.sleep(100);
        thread3.start();
    }
    public static void testObjectLock() throws InterruptedException {
        SynchronizedTest test_A = new SynchronizedTest("test_A");
        SynchronizedTest test_B = new SynchronizedTest("test_B");

        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    test_A.synchObjectMethodA();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"thread_1");
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    test_A.synchObjectMethodA();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"thread_2");

        Thread thread3 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    test_B.synchObjectMethodA();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"thread_3");

        thread1.start();
        Thread.sleep(100);
        thread2.start();
        Thread.sleep(100);
        thread3.start();
    }

    public static void testContentLock() throws InterruptedException {
        SynchronizedTest test_A = new SynchronizedTest("test_A");
        SynchronizedTest test_B = new SynchronizedTest("test_B");

        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    test_A.synchMethodBody();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"thread_1");
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    test_A.synchMethodBody();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"thread_2");

        Thread thread3 = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    test_B.synchMethodBody();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        },"thread_3");

        thread1.start();
        Thread.sleep(100);
        thread2.start();
        Thread.sleep(100);
        thread3.start();
    }
    /***
     * synchronized修改对象方法，则锁是对象锁，锁在具体的对象上，哪个对象调用，就加在哪个对象上
     *  synchObjectMethodA方法执行完，就会释放这个锁
     */
    public synchronized void synchObjectMethodA() throws InterruptedException {
        System.out.println(Thread.currentThread().getName()+"=======synchronized synchObjectMethodA===="+this.getName()+"======开始获得锁");
        Thread.sleep(5000);
        System.out.println(Thread.currentThread().getName()+"=========synchronized synchObjectMethodA======"+this.getName()+"====整个方法执行结束后会自动释放锁");
    }

    public synchronized void synchObjectMethodB() throws InterruptedException {
        System.out.println(Thread.currentThread().getName()+"=========synchronized synchObjectMethodB====="+this.getName()+"=====开始获得锁");
        System.out.println(Thread.currentThread().getName()+"=========synchronized synchObjectMethodB====="+this.getName()+"=====整个方法执行结束后会自动释放锁");
    }
    public static synchronized void synchClassMethodA(SynchronizedTest test) throws InterruptedException {
        System.out.println(Thread.currentThread().getName()+"=========synchronized synchClassMethodA===="+test.getName()+"======开始获得锁：SynchronizedTest.class");
        Thread.sleep(5000);
        System.out.println(Thread.currentThread().getName()+"=========synchronized synchClassMethodA===="+test.getName()+"======整个方法执行结束后会自动释放锁：SynchronizedTest.class");
    }
    /***
     * synchronized修改static方法，则锁是加在对象SynchronizedTest.class上
     *   synchClassMethodB 方法执行完，就会释放这个锁
     */
    public static synchronized void synchClassMethodB(SynchronizedTest test){
        System.out.println(Thread.currentThread().getName()+"=========synchronized synchClassMethodB====="+test.getName()+"=====开始获得锁：SynchronizedTest.class");

        System.out.println(Thread.currentThread().getName()+"=========synchronized synchClassMethodB====="+test.getName()+"=====整个方法执行结束后会自动释放锁：SynchronizedTest.class");
    }



    public void synchMethodBody() throws InterruptedException {
        //开始加锁
        System.out.println(Thread.currentThread().getName()+"=========synchronized synchMethodBody====="+this.getName()+"=====开始获得锁：obj");
        synchronized (obj){

            System.out.println("synchronized synchMethodBody");
            Thread.sleep(5000);
        }
        //释放锁
        System.out.println(Thread.currentThread().getName()+"=========synchronized synchMethodBody===="+this.getName()+"======这部分方法块执行完后自动释放锁：obj");
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
