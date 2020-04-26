package com.github.learn.java.util.concurrent.executorservice;

/**
 * @author zhanfeng.zhang
 * @date 2019/11/06
 */
public interface AService {

    /**
     * 用于异步处理的任务一般都是没有返回值的
     *
     * @param params
     */
    void doSomething(String params);
}
