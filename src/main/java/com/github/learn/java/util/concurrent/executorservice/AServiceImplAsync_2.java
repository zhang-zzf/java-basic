package com.github.learn.java.util.concurrent.executorservice;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import lombok.extern.slf4j.Slf4j;

/**
 * @author zhanfeng.zhang
 * @date 2019/11/06
 */
@Slf4j
public class AServiceImplAsync_2 implements AService {

    // 自己独享的线程池，不和别的对象公用
    // 用于特殊要求的业务场景

    // alibaba 不容许使用Executors创建线程池
    // private final ExecutorService threadPool = Executors.newCachedThreadPool();
    private final ExecutorService threadPool;

    {
        final int corePoolSize = 1;
        final int maximumPoolSize = Runtime.getRuntime().availableProcessors();
        final long keepAliveTime = 60;
        final TimeUnit unit = TimeUnit.SECONDS;
        // 无界队列
        final BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(Integer.MAX_VALUE);
        // 用于创建线程池中的线程
        final ThreadFactory threadFactory = r -> new Thread(r, "threadPool MUST have a name");
        // 拒绝策略
        final RejectedExecutionHandler handler = new ThreadPoolExecutor.CallerRunsPolicy();
        threadPool = new ThreadPoolExecutor(
            corePoolSize,
            maximumPoolSize,
            keepAliveTime, unit,
            workQueue,
            threadFactory,
            handler
        );
    }

    @Override
    public void doSomething(String params) {
        threadPool.submit(() -> {
            // doSomeBusinessLogic();
            log.info("{} log", Thread.currentThread());
        });
    }
}
