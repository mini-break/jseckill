package com.my;

import com.google.common.util.concurrent.RateLimiter;

import java.util.concurrent.TimeUnit;

/**
 * @Description
 * @Author hg
 * @Date 2019-05-31 15:24
 */
public class TokenBucketTest {
    public static void main(String[] args) {
        RateLimiter rateLimiter = RateLimiter.create(5,1000, TimeUnit.MILLISECONDS);
        for (int i = 0; i < 5; i++) {
            System.out.println(rateLimiter.acquire());
        }
    }
}
