package com.online.common.util;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class TokenBucket {

    // 默认桶大小个数 即最大瞬间流量是64M
    private static final int DEFAULT_BUCKET_SIZE = 1024 * 1024 * 64;

    // 一个桶的单位是1字节
    private int everyTokenSize = 1;

    // 瞬间最大流量
    private int maxFlowRate;

    // 平均流量
    private int avgFlowRate;

    // 队列来缓存桶数量：最大的流量峰值就是 = everyTokenSize*DEFAULT_BUCKET_SIZE 64M = 1 * 1024 *
    // 1024 * 64
    private ArrayBlockingQueue<Byte> tokenQueue = new ArrayBlockingQueue<Byte>(
            DEFAULT_BUCKET_SIZE);

    private ScheduledExecutorService scheduledExecutorService = Executors
            .newSingleThreadScheduledExecutor();

    private volatile boolean isStart = false;

    private ReentrantLock lock = new ReentrantLock(true);

    private static final byte A_CHAR = 'a';

    public TokenBucket() {
    }

    public TokenBucket(int maxFlowRate, int avgFlowRate) {
        this.maxFlowRate = maxFlowRate;
        this.avgFlowRate = avgFlowRate;
    }

    public TokenBucket(int everyTokenSize, int maxFlowRate, int avgFlowRate) {
        this.everyTokenSize = everyTokenSize;
        this.maxFlowRate = maxFlowRate;
        this.avgFlowRate = avgFlowRate;
    }

    public void addTokens(Integer tokenNum) {

        // 若是桶已经满了，就不再家如新的令牌
        for (int i = 0; i < tokenNum; i++) {
            tokenQueue.offer(Byte.valueOf(A_CHAR));
        }
    }

    public TokenBucket build() {

        start();
        return this;
    }

    /**
     * 获取足够的令牌个数
     *
     * @return
     */
    public boolean getTokens(int count) {


        int needTokenNum = count;// 传输内容大小对应的桶个数

        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            boolean result = needTokenNum <= tokenQueue.size(); // 是否存在足够的桶数量
            if (!result) {
                return false;
            }

            int tokenCount = 0;
            for (int i = 0; i < needTokenNum; i++) {
                Byte poll = tokenQueue.poll();
                if (poll != null) {
                    tokenCount++;
                }
            }

            return tokenCount == needTokenNum;
        } finally {
            lock.unlock();
        }
    }

    public void start() {

        // 初始化桶队列大小
        if (maxFlowRate != 0) {
            tokenQueue = new ArrayBlockingQueue<Byte>(maxFlowRate);
        }

        // 初始化令牌生产者,并设置生成流量的速度
        TokenProducer tokenProducer = new TokenProducer(avgFlowRate, this);
        //每秒创建avgFlowRate令牌
        scheduledExecutorService.scheduleAtFixedRate(tokenProducer, 0, 1,
                TimeUnit.SECONDS);
        isStart = true;

    }

    public void stop() {
        isStart = false;
        scheduledExecutorService.shutdown();
    }

    public boolean isStarted() {
        return isStart;
    }

    class TokenProducer implements Runnable {

        private int avgFlowRate;
        private TokenBucket tokenBucket;

        public TokenProducer(int avgFlowRate, TokenBucket tokenBucket) {
            this.avgFlowRate = avgFlowRate;
            this.tokenBucket = tokenBucket;
        }

        @Override
        public void run() {
            tokenBucket.addTokens(avgFlowRate);
        }
    }

    public static TokenBucket newBuilder() {
        return new TokenBucket();
    }

    public TokenBucket everyTokenSize(int everyTokenSize) {
        this.everyTokenSize = everyTokenSize;
        return this;
    }

    public TokenBucket maxFlowRate(int maxFlowRate) {
        this.maxFlowRate = maxFlowRate;
        return this;
    }

    public TokenBucket avgFlowRate(int avgFlowRate) {
        this.avgFlowRate = avgFlowRate;
        return this;
    }

    private String stringCopy(String data, int copyNum) {

        StringBuilder sbuilder = new StringBuilder(data.length() * copyNum);

        for (int i = 0; i < copyNum; i++) {
            sbuilder.append(data);
        }

        return sbuilder.toString();

    }

    public static void main(String[] args) throws IOException,
            InterruptedException {

        tokenTest();
    }

    private static void arrayTest() {
        ArrayBlockingQueue<Integer> tokenQueue = new ArrayBlockingQueue<Integer>(
                10);
        tokenQueue.offer(1);
        tokenQueue.offer(1);
        tokenQueue.offer(1);
        System.out.println(tokenQueue.size());
        System.out.println(tokenQueue.remainingCapacity());
    }

    private static void tokenTest() throws InterruptedException, IOException {
        TokenBucket tokenBucket = TokenBucket.newBuilder().avgFlowRate(512)
                .maxFlowRate(1024).build();

        String data = "xxxx";// 四个字节
        for (int i = 1; i <= 1000; i++) {

            Random random = new Random();
            int i1 = random.nextInt(100);
            boolean tokens = tokenBucket.getTokens(10);
            TimeUnit.MILLISECONDS.sleep(100);
            if (tokens) {
                System.out.println("token pass --- index:" + i1);
            } else {
                System.out.println("token rejuect --- index" + i1);
            }

        }
    }

}
