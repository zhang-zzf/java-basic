package com.github.learn.idea;

import org.intellij.lang.annotations.Language;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

/**
 * @author zhanfeng.zhang
 * @date 2020/5/1
 */
public class BasicTest {

    @Test
    void testInjectLanguageOfReferenceOfJson() {
        @Language("JSON") String json = "{\n" +
            "  \"name\": \"zhanfeng.zhang\",\n" +
            "  \"age\": 32,\n" +
            "  \"sex\": \"male\",\n" +
            "  \"values\": []\n" +
            "}";
    }

    @Disabled
    @Test
    void testLiveTemplate(int a, String str) {
        // fori
        for (int i = 0; i < 10; i++) {

        }
        // foreach
        for (String name : Arrays.asList("a")) {

        }
        // fixme
        // FIXME: 2020/5/1
        // w
        while (true) {

        }
    }

}
