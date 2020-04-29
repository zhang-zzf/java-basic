package com.github.learn.java.util.concurrent.executorservice;

import lombok.extern.slf4j.Slf4j;

/**
 * @author zhanfeng.zhang
 * @date 2019/11/06
 */
@Slf4j
public class AServiceImplAsync_3Impl implements AService {

    // 使用应用级别公用的线程池

    @Override
    public void doSomething(String params) {
        ApplicationThreadPool.threadPool().submit(() -> {
            // doSomeBusinessLogic();
            log.info("{} log", Thread.currentThread());
        });
    }
}
