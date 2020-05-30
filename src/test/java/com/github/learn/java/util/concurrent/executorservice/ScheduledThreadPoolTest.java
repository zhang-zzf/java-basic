package com.github.learn.java.util.concurrent.executorservice;

import lombok.extern.slf4j.Slf4j;
import org.junit.Ignore;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author zhanfeng.zhang
 * @date 2020/5/30
 */
@Slf4j
public class ScheduledThreadPoolTest {

    @Ignore
    @Test
    public void test() throws InterruptedException {
        ScheduledExecutorService scheduled = Executors.newScheduledThreadPool(1);

        scheduled.scheduleAtFixedRate(() -> {
            String tName = Thread.currentThread().getName();
            log.info("{} start at {}", tName, LocalTime.now());
            try {
                Thread.sleep(2 * 1000);
            } catch (InterruptedException e) {
            }
            log.info("{} start end {}", tName, LocalTime.now());
        }, 0, 1, TimeUnit.SECONDS);

        Thread.currentThread().join();
    }

    @Ignore
    @Test
    public void test2() throws InterruptedException {
        ScheduledExecutorService scheduled = Executors.newScheduledThreadPool(1);

        scheduled.scheduleWithFixedDelay(() -> {
            String tName = Thread.currentThread().getName();
            log.info("{} start at {}", tName, LocalTime.now());
            try {
                Thread.sleep(2 * 1000);
            } catch (InterruptedException e) {
            }
            log.info("{} end at {}", tName, LocalTime.now());
        }, 0, 1, TimeUnit.SECONDS);

        Thread.currentThread().join();
    }
}

