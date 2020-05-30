package com.github.learn.threads.visibility;

import java.io.Closeable;

import lombok.extern.slf4j.Slf4j;

/**
 * @author zhang.zzf
 * @date 2020-04-24
 */
@Slf4j
public class NoVisibility implements Closeable {

    private boolean shutdown = false;

    @Override
    public void close() {
        shutdown = true;
        log.info("shutdown -> true");
    }

    public void doWork() {
        while (!shutdown) {
            // do stuff
        }
        System.out.println("shutdown now...");
    }

    /**
     * Conclusion: work-thread will never be shutdown.
     */
    public static void main(String[] args) throws InterruptedException {
        NoVisibility statusFlag = new NoVisibility();

        new Thread(statusFlag::doWork, "work-thread").start();
        Thread.sleep(1000);

        new Thread(statusFlag::close, "close-thread").start();
    }

}
