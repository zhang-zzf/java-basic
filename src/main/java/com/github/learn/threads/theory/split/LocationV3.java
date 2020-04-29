package com.github.learn.threads.theory.split;

/**
 * 乐观锁/cas算法
 *
 * @author zhangzhanfeng
 * @date 2020/4/29
 */
public class LocationV3 {
    /**
     * 有this对象加锁保护
     */
    private XY xy;

    public LocationV3(double x, double y) {
        xy = new XY(x, y);
    }

    public synchronized XY xy() {
        return xy;
    }

    /**
     * 必须加锁保证原子操作
     *
     * @author zhanfeng.zhang
     */
    private synchronized boolean compareAndSet(XY oldValue, XY newValue) {
        boolean success = oldValue == xy;
        if (success) {
            xy = newValue;
        }
        return success;
    }

    /**
     * 减小锁的粒度，把创建对象放到了锁的外面。
     *
     * @author zhanfeng.zhang
     */
    public void moveBy(double dx, double dy) {
        while (!Thread.interrupted()) {
            // 这里有锁保护
            XY oldValue = xy();
            // 这里无锁
            XY newValue = new XY(oldValue.x() + dx, oldValue.y() + dx);
            // 这里有锁保护
            if (compareAndSet(oldValue, newValue)) {
                break;
            }
            Thread.yield();
        }
    }
}
