package com.github.learn.java.util.concurrent.executorservice;

import lombok.extern.slf4j.Slf4j;

/**
 * @author zhanfeng.zhang
 * @date 2019/11/06
 */
@Slf4j
public class AServiceImpl implements AService {

    @Override
    public void doSomething(String params) {
        // 如何把次方法内的逻辑异步处理
        // doSomeBusinessLogic();
        log.info("{} log", Thread.currentThread());
    }
}
