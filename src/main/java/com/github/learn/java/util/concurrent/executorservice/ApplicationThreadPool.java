package com.github.learn.java.util.concurrent.executorservice;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * 单例
 *
 * @author zhanfeng.zhang
 * @date 2019/11/06
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ApplicationThreadPool {

    private static final class InstanceHolder {
        private static final ExecutorService THREAD_POOL;

        static {
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
            THREAD_POOL = new ThreadPoolExecutor(
                corePoolSize,
                maximumPoolSize,
                keepAliveTime, unit,
                workQueue,
                threadFactory,
                handler
            );
        }
    }

    public static final ExecutorService threadPool() {
        return InstanceHolder.THREAD_POOL;
    }

}
