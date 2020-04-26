package com.github.learn.threads.lockfree;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import org.junit.jupiter.api.Test;

/**
 * @author zhanfeng.zhang
 * @date 2020/04/19
 */
class MaxValTest {

    private static final int CAPACITY = 1024;
    final MaxVal maxVal = new MaxVal();

    @Test
    void setMax() throws InterruptedException {

        // 准备数据集
        List<Integer> data = new ArrayList<>(CAPACITY);
        for (int i = 0; i < CAPACITY; i++) {
            data.add(new Random().nextInt(512));
        }
        Integer max = data.stream().max(Comparator.naturalOrder()).get();

        // 5个线程同时启动
        CountDownLatch startGate = new CountDownLatch(1);
        CountDownLatch endGate = new CountDownLatch(5);
        for (int i = 0; i < 5; i++) {
            new Thread(() -> {
                try {
                    startGate.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                data.stream().forEach(d -> maxVal.setMax(d));
                endGate.countDown();
            }).start();
        }
        // 启动线程
        startGate.countDown();

        // 等待5个线程全部执行完毕
        endGate.await();
        assertThat(maxVal.getMax()).isEqualTo(max);
    }
}