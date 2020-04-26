package com.github.learn.java.util.concurrent.executorservice;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

/**
 * @author zhanfeng.zhang
 * @date 2020/04/19
 */
@Slf4j
public class ThreadPoolTest {

    static class TimedTask implements Runnable {

        public static final AtomicInteger count = new AtomicInteger(0);

        private final int taskNo;
        private final int sleepTime;

        public TimedTask(int sleepTime) {
            this.sleepTime = sleepTime;
            taskNo = count.getAndIncrement();
        }

        @Override
        public void run() {
            log.info("Thread: {}, taskNo: {} => start", Thread.currentThread(), taskNo);
            try {
                TimeUnit.SECONDS.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.info("Thread: {}, taskNo: {} => end", Thread.currentThread(), taskNo);
        }
    }

    /**
     * 测试线程池收缩
     */
    @Test
    public void testShrink() {
        ThreadPoolExecutor pool = new ThreadPoolExecutor(2,
            4,
            8, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(4),
            new CallerRunsPolicy());
        // core
        pool.submit(new TimedTask(8));
        pool.submit(new TimedTask(8));
        // queue
        pool.submit(new TimedTask(8));
        pool.submit(new TimedTask(8));
        pool.submit(new TimedTask(8));
        pool.submit(new TimedTask(8));
        // max
        pool.submit(new TimedTask(10));
        pool.submit(new TimedTask(10));
        // 处理 queue
        log.info("");
    }

    /**
     * 测试线程池如何扩张
     */
    @Test
    public void testGrow() {
        ThreadPoolExecutor pool = new ThreadPoolExecutor(2,
            4,
            8, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(2),
            new CallerRunsPolicy());

        // core size
        pool.submit(new Task());
        pool.submit(new Task());
        // queue
        pool.submit(new Task());
        pool.submit(new Task());
        // maximum
        pool.submit(new Task());
        pool.submit(new Task());
        // eject policy
        pool.submit(new Task());
    }

    static class Task implements Runnable {

        public static final AtomicInteger count = new AtomicInteger(0);

        private final int taskNo;

        public Task() {
            taskNo = count.getAndIncrement();
        }

        @Override
        public void run() {
            log.info("Thread: {} -> taskNo: {}", Thread.currentThread(), taskNo);
            try {
                Thread.currentThread().join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
