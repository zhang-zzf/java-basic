package com.github.learn;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.github.learn.util.HexUtil.*;
import static java.nio.charset.StandardCharsets.*;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.BDDAssertions.*;

/**
 * @author zhanfeng.zhang
 * @date 2020/04/24
 */
@Slf4j
public class JavaBasicTest {

    @Test
    void testExceptionFinally() {
        MutableObject o = new MutableObject();
        Throwable throwable = catchThrowable(() -> aMethodThrowException(o));
        then(throwable).isInstanceOf(RuntimeException.class);
        // finally will always execute
        then(o.i).isEqualTo(1);
    }

    private void aMethodThrowException(MutableObject object) {
        try {
            throw new RuntimeException();
        } catch (RuntimeException e) {
            throw e;
        } finally {
            object.i = 1;
        }
    }

    @Test
    void testMapEquals() {
        Map<Byte, String> map = new HashMap<Byte, String>();
        byte b = 1;
        final String V = "Hello, World";
        map.put(b, V);
        // compile error: int can not wrap into Byte
        // map.put(2, "");
        // 1 (int) -> Integer
        // @see java.lang.Byte#equals
        then(map.get(1)).isNotEqualTo(V).isNull();
    }

    @Test
    void testStringSplit() {
        String str = "f,f , f , f";
        then(str.split(",")).containsExactly("f", "f ", " f ", " f");
        String hostAndPort = "192.168.0.140:8880";
        then(hostAndPort.split(",")).containsExactly(hostAndPort);
    }

    /**
     * NullPointException
     */
    @Test
    void testNull2Primitive() {
        Boolean bNull = null;
        Throwable throwable = catchThrowable(() -> {
            boolean pbNull = bNull;
        });
        then(throwable).isInstanceOf(NullPointerException.class);
    }

    @Test
    void testStringEncodingAndDecoding() throws UnsupportedEncodingException {
        String str = "张占峰";
        String strHexOfUtf8 = "E5 BC A0 E5 8D A0 E5 B3 B0";
        String strHexOfGbk = "D5 C5 D5 BC B7 E5";
        // encode use ISO_8859_1 "张占峰" => "???"
        String strHexOfAscii = "3F 3F 3F";
        then(toHex(str.getBytes(UTF_8))).isEqualTo(strHexOfUtf8);
        then(toHex(str.getBytes("gbk"))).isEqualTo(strHexOfGbk);
        then(toHex(str.getBytes(ISO_8859_1))).isEqualTo(strHexOfAscii);

        String decodeStr = "你好";
        String strOfWrongDecode = "浣犲ソ";
        // encode "你好" => "E4 BD A0 E5 A5 BD";
        byte[] bs = decodeStr.getBytes(UTF_8);
        // decode "E4 BD A0 E5 A5 BD" => "浣犲ソ"
        String str2 = new String(bs, "gbk");
        then(str2).isNotEqualTo(decodeStr).isEqualTo(strOfWrongDecode);
    }

    @Test
    void testStringReplace() {
        // str 字面量 'Hell\o world'
        String str = "Hell\\o world";
        then(str).isEqualTo("Hell\\o world");
        // "\\\\" 字面量 '\\' 代表正则的一个 '\'
        then(str.replaceAll("\\\\", "/")).isEqualTo("Hell/o world");
    }

    @Test
    void testList() {
        String[] dataArray = { "a", "b", "c", "d", "e", "f" };
        // can not add or remove elements to the constantList
        List<String> constantList = Arrays.asList(dataArray);

        List<String> list = new ArrayList<>(8);
        list.addAll(constantList);
        for (int i = 0; i < list.size(); i++) {
            if (i % 2 == 0) {
                list.remove(i);
            }
        }
        then(list).containsExactly("b", "c", "e", "f");
    }

    @Test
    void testAutoBox() {
        Integer i1 = 100, i2 = 100, i3 = 150, i4 = 150;
        then(i1).isSameAs(i2).isEqualTo(i2);
        then(i3).isNotSameAs(i4).isEqualTo(i4);
    }

    @Test
    void testString() {
        String s1 = "Programming";
        String s2 = "Programming";
        String s3 = "Program" + "ming";
        then(s1).isSameAs(s2).isSameAs(s2.intern())
                .isSameAs(s3).isSameAs(s3.intern());
    }

    @Test
    void nullIsInstanceOfObject() {
        then(null instanceof Object).isFalse();
    }


    class MutableObject {
        int i = 0;
    }

}
