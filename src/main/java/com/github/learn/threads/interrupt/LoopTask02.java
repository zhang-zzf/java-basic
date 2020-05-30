package com.github.learn.threads.interrupt;

/**
 * @author zhanfeng.zhang
 * @date 2020/5/30
 */
public class LoopTask02 implements Runnable {
    @Override
    public void run() {
        while (true) {
            if (Thread.interrupted()) {
                break;
            }
            // do some business staff...

            if (Thread.interrupted()) {
                break;
            }
            // do some other business staff..
        }
    }
}
