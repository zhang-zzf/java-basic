package com.github.learn.threads.interrupt;

/**
 * @author zhanfeng.zhang
 * @date 2020/5/30
 */
public class LoopTask01 implements Runnable {
    @Override public void run() {
        while (true) {
            // do some business staff...
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                // 这里必须根据业务场景做响应的处理
                break; // 线程被中断后，跳出循环
            }
        }
    }
}
