package com.online.zk.util;

import org.apache.zookeeper.*;
import org.apache.zookeeper.data.Stat;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * zookeeper分布式锁
 * @author zhifang.xu
 */
public class ZooKeeperDistributedLock implements Watcher{
    private ZooKeeper zk;
    private String locksRoot = "/locks";
    private String lockId;
    private String waitNode;
    private String lockNode;
    private CountDownLatch latch;
    private CountDownLatch connectedLatch = new CountDownLatch(1);
    private int sessionTimeout = 30000;

    public ZooKeeperDistributedLock(String lockId) {
        this.lockId = lockId;
        try {
            String address = "192.168.31.187:2181,192.168.31.19:2181,192.168.31.227:2181";
            zk = new ZooKeeper(address, sessionTimeout, this);//这个连接会把本身作为一个监听器注册进来
            connectedLatch.await();//不让它终止
        } catch (IOException e) {
            throw new LockException(e);
        }catch (InterruptedException e) {
            throw new LockException(e);
        }
    }

    /**
     *
     * @param event
     */
    public void process(WatchedEvent event) {
        if (event.getState() == Event.KeeperState.SyncConnected) {
            connectedLatch.countDown();//连接到zookeeper后的话，会解除上面的connectedLatch.await()
            return;
        }

        if (this.latch != null) {//如果有线程等待当前节点的变化，则唤醒
            this.latch.countDown();
        }
    }

    /***
     * 获得分布式锁
     */
    public void acquireDistributedLock() {
        try {
            if (this.tryLock()) {
                return;
            } else {
                //进程获得等待的节点
                waitForLock(waitNode, sessionTimeout);
            }
        } catch (KeeperException e) {
            throw new LockException(e);
        } catch (InterruptedException e) {
            throw new LockException(e);
        }
    }

    /***
     *
     * @return
     * 1、根据每个进程的productId来创建一个临时节点
     * 2、获得/locks下的所有子节点并按照名称排序
     * 3、判断当前节点是在子节点列表中的位置
     * 4、如果当前节点在子节点列表中不是第一位，则表示获得锁失败，则会监听排在当前节点的前一个节点
     */
    public boolean tryLock() {
        try {
            // 传入进去的locksRoot + “/” + productId
            // 假设productId代表了一个商品id，比如说1
            // locksRoot = locks
            // /locks/10000000000，/locks/10000000001，/locks/10000000002
            lockNode = zk.create(locksRoot + "/" + lockId, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);

            // 看看刚创建的节点是不是最小的节点
            // locks：10000000000，10000000001，10000000002
            //获得所有子节点，并从小到大进行排序
            List<String> locks = zk.getChildren(locksRoot, false);
            Collections.sort(locks);
            //比较当前节点是不是最小节点，因为排序后，get(0)获得是最小的节点

            if(lockNode.equals(locksRoot+"/"+ locks.get(0))){
                //如果是最小的节点,则表示取得锁
                return true;
            }

            //如果不是最小的节点，找到比自己小1的节点
            int previousLockIndex = -1;
            for(int i = 0; i < locks.size(); i++) {//遍历所有的子节点
                //对于每个进程，lockNote值不一样：
                //进程2：lockNode=10000000001==>i=1,所以进程2等待爹节点是1=0，通过locks.get(previousLockIndex);获得
                //进程3：lockNode=10000000002==>i=2
                if(lockNode.equals(locksRoot + "/" + locks.get(i))) {
                    previousLockIndex = i - 1;
                    break;
                }
            }
            //获得当前进程等待的节点
            //进程2：lockNode=10000000001==>i=1,所以previousLockIndex=0；所以它监视0点节点：locks.get(previousLockIndex)
            //进程1：lockNode=10000000002==>i=2,所以previousLockIndex=1；所以它监视1点节点：locks.get(previousLockIndex)

            this.waitNode = locks.get(previousLockIndex);
        } catch (KeeperException e) {
            throw new LockException(e);
        } catch (InterruptedException e) {
            throw new LockException(e);
        }
        return false;
    }

    /***
     *
     * @param waitNode 当前进程监视的前一个节点
     * @param waitTime
     * @return
     * @throws InterruptedException
     * @throws KeeperException
     */
    private boolean waitForLock(String waitNode, long waitTime) throws InterruptedException, KeeperException {
        //判断前一个节点是否存在，并监视
        Stat stat = zk.exists(locksRoot + "/" + waitNode, true);
        if (stat != null) {//前一个节点存在的话，则挂起等待waitTime秒
            this.latch = new CountDownLatch(1);
            this.latch.await(waitTime, TimeUnit.MILLISECONDS);
            this.latch = null;
        }
        return true;
    }

    public void unlock() {
        try {
            // 删除/locks/10000000000节点
            // 删除/locks/10000000001节点
            System.out.println("unlock " + lockNode);
            zk.delete(lockNode, -1);
            lockNode = null;
            zk.close();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (KeeperException e) {
            e.printStackTrace();
        }
    }

    public class LockException extends RuntimeException {
        private static final long serialVersionUID = 1L;

        public LockException(String e) {
            super(e);
        }

        public LockException(Exception e) {
            super(e);
        }
    }

    public static void main(String[] args) {
        ZooKeeperDistributedLock lock = new ZooKeeperDistributedLock("xuzf880707");
        lock.acquireDistributedLock();
    }
}
