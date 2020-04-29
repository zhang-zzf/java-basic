package com.github.learn.threads.theory.split;


import com.github.learn.threads.annotation.ThreadSafe;

/**
 * 悲观锁
 *
 * @author zhangzhanfeng
 * @date Dec 10, 2017
 */
@ThreadSafe(authors = {"zhanfeng.zhang"})
public class LocationV2 {

    private XY xy;

    public LocationV2(double x, double y) {
        xy = new XY(x, y);
    }

    public synchronized XY xy() {
        return xy;
    }

    public synchronized void movedBy(double dx, double dy) {
        XY oldXy = xy();
        xy = new XY(oldXy.x() + dx, oldXy.y() + dy);
    }

}
