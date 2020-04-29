package com.github.learn.java.util.concurrent.executorservice;

import lombok.extern.slf4j.Slf4j;

/**
 * @author zhanfeng.zhang
 * @date 2019/11/06
 */
@Slf4j
public class AServiceImplAsync_1Impl implements AService {
    @Override
    public void doSomething(String params) {
        // 自己new线程, 不鼓励，创建线程和销毁线程需要大量资源
        // 有可能线程创建太多耗尽系统资源
        new Thread(() -> {
            // doSomeBusinessLogic();
            log.info("{} log", Thread.currentThread());
        }, "thread name must have a name").start();
    }
}
