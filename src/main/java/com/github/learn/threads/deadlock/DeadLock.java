package com.github.learn.threads.deadlock;

public class DeadLock {

    public static void main(String... args) throws InterruptedException {
        Object lock1 = new Object();
        Object lock2 = new Object();

        synchronized (lock1) {
            new Thread(() -> {
                synchronized (lock2) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    synchronized (lock1) {
                        // do something;
                        System.out.println("newThread");
                    }
                }
            }).start();
            Thread.sleep(1000);
            synchronized (lock2) {
                System.out.println("mainThread");
            }
        }
    }
}
