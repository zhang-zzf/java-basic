package com.github.learn.java.util;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.BDDAssertions.then;

/**
 * @author zhanfeng.zhang
 * @date 2020/5/13
 */
public class Jdk8MapTest {

    /**
     * Map<K,Collection<V>>  K -> Collection<V>
     */
    @Test void givenComputeIfAbsent_when_then() {
        final String name = "zzf";
        final String email1 = "zhanfeng.zhang@icloud.com";
        final String email2 = "zhanfeng.zhang.ga@gmail.com";
        Map<String, Set<String>> maps = new HashMap<>();
        maps.computeIfAbsent(name, key -> new HashSet<>()).add(email1);
        maps.computeIfAbsent(name, key -> new HashSet<>()).add(email2);
        then(maps.get(name)).contains(email1, email2);
    }
}
