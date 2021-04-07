package com.my;

import java.util.Random;

/**
 * 漏桶算法
 */
public class LeakyBucket {

    // 总容量
    private final long capacity;
    // 漏水速率
    private final long leaksIntervalInMillis;

    // 桶中已有
    private double used;
    // 最近处理请求处理的时间
    private long lastLeakTimestamp;

    public LeakyBucket(long capacity, long leaksIntervalInMillis) {
        this.capacity = capacity;
        this.leaksIntervalInMillis = leaksIntervalInMillis;

        this.used = 0;
        this.lastLeakTimestamp = System.currentTimeMillis();
    }

    /**
     * 消费请求
     *
     * @param drop 以一定速率(drop)往桶中滴水
     * @return
     */
    synchronized public boolean tryConsume(int drop) {
        leak();

        // 超过桶容量则限流
        if (used + drop > capacity) {
            return false;
        }

        // 未超过则放入桶中
        used = used + drop;
        return true;
    }

    /**
     * 按固定速率处理请求
     */
    private void leak() {
        // 当前时间戳
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis > lastLeakTimestamp) {
            long millisSinceLastLeak = currentTimeMillis - lastLeakTimestamp;
            long leaks = millisSinceLastLeak / leaksIntervalInMillis;
            if (leaks > 0) {
                if (used <= leaks) {
                    used = 0;
                } else {
                    used -= (int) leaks;
                }
                this.lastLeakTimestamp = currentTimeMillis;
            }
        }
    }

    public static void main(String[] args) {
        LeakyBucket leakyBucket = new LeakyBucket(100, 2);
        for (int i = 0; i < 100; i++) {
            int rand = new Random().nextInt(5) + 1;
            System.out.println(rand + " : " + leakyBucket.tryConsume(rand));
        }
    }
}