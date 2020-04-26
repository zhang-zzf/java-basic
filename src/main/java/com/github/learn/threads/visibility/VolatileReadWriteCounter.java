package com.github.learn.threads.visibility;


import com.github.learn.threads.annotation.GuardedBy;
import com.github.learn.threads.annotation.ThreadSafe;

@ThreadSafe
public class VolatileReadWriteCounter {

    /**
     * Employs a cheap read-write lock trick
     * <p>All mutative operations MUST be done with the 'this' lock held.</p>
     */
    @GuardedBy("this")
    private volatile int counter = 0;

    public int get() {
        return counter;
    }

    /**
     * 保证只有一个线程更新volatile值
     */
    public synchronized void increment() {
        counter++;
    }
}
