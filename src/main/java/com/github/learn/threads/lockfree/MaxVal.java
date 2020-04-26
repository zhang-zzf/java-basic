package com.github.learn.threads.lockfree;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zhanfeng.zhang
 * @date 2020/04/19
 */
public class MaxVal {

    private final AtomicInteger val = new AtomicInteger();

    public int getMax() {
        return val.get();
    }

    public void setMax(int newMax) {
        while (true) {
            int oldMax = val.get();
            if (newMax <= oldMax) {
                break;
            }
            if (val.compareAndSet(oldMax, newMax)) {
                break;
            }
        }
    }

}
