package com.github.learn.java.util.concurrent.executorservice;

import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;
import org.junit.Test;

import java.util.concurrent.*;
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author zhanfeng.zhang
 * @date 2020/04/19
 */
@Slf4j
@Ignore
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
     * {@link ExecutorService#shutdown()} 不会中断正在执行任务的线程
     * <p>1. 不能提交新的task</p>
     * <p>2. 等待已经提交的task执行完成</p>
     * <p>3. 关闭线程池</p>
     */
    @Test
    public void testShutdown() throws InterruptedException {
        ExecutorService service = Executors.newCachedThreadPool();
        // This method does not wait for previously submitted tasks to complete execution.
        // Use awaitTermination to do tha
        service.shutdown();
        while (true) {
            // 等待线程池关闭
            boolean poolTerminated = service.awaitTermination(1, TimeUnit.SECONDS);
            if (poolTerminated)
                break;

        }
    }

    /**
     * {@link ExecutorService#shutdownNow()} 会中断正在执行任务的线程
     * <p>1. 当前线程发送 interrupt 信号给所有在执行任务的线程</p>
     * <p>2. 返回所有在等待队列中还未执行的任务</p>
     *
     * <p>正在执行任务的线程会不会停止依赖与提交的loopTask是否响应中断</p>
     */
    @Test
    public void testShutdownNow() throws InterruptedException {
        ExecutorService service = Executors.newScheduledThreadPool(1);
        // This method does not wait for actively executing tasks to terminate.
        // Use awaitTermination to do that.
        service.shutdownNow();
        while (true) {
            // 等待线程池关闭
            boolean poolTerminated = service.awaitTermination(1, TimeUnit.SECONDS);
            if (poolTerminated)
                break;
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
