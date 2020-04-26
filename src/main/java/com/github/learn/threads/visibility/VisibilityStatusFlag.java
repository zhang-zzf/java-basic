package com.github.learn.threads.visibility;

import java.io.Closeable;

/**
 * @author zhang.zzf
 * @date 2020-04-24
 */
public class VisibilityStatusFlag implements Closeable {

    private volatile boolean shutdown = false;

    @Override
    public void close() {
        shutdown = true;
    }

    public void doWork() {
        while (!shutdown) {
            // do stuff
        }
        System.out.println("shutdown now...");
    }


    public static void main(String[] args) throws InterruptedException {
        VisibilityStatusFlag statusFlag = new VisibilityStatusFlag();

        new Thread(statusFlag::doWork, "work-thread").start();
        Thread.sleep(1000);

        new Thread(statusFlag::close, "close-thread").start();

        // main sleep for a period time for watch other threads state.
        Thread.sleep(10000);
    }
}
