package com.github.learn.threads.theory.split;


import com.github.learn.threads.annotation.ThreadSafe;

/**
 * 此类是线程安全的，但并不能保证语义的正确性
 *
 * @author zhangzhanfeng
 * @date Dec 10, 2017
 */
@ThreadSafe(authors = {"zhanfeng.zhang"})
public class Location {

    private double x, y;

    public Location(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public synchronized double x() {
        return x;
    }

    public synchronized double y() {
        return y;
    }

    public synchronized void moveBy(double dx, double dy) {
        x = x + dx;
        y = y + dy;
    }

}
