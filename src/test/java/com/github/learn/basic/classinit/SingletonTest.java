package com.github.learn.basic.classinit;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.BDDAssertions.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author zhanfeng.zhang
 * @date 2020/05/06
 */
class SingletonTest {

    @Test void testClassInit() {
        Singleton instance = Singleton.getInstance();
        then(instance.c1).isEqualTo(1);
        then(instance.c2).isEqualTo(0);
    }

}