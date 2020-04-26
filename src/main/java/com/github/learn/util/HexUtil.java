package com.github.learn.util;

/**
 * @author zhang.zzf
 * @date 2020-04-26
 */
public class HexUtil {

    public static String toHex(byte[] bs) {
        StringBuilder s = new StringBuilder();
        for (byte b : bs) {
            String str = Integer.toHexString(b & 0xFF);
            if ((b & 0xF0) == 0) {
                s.append('0').append(str);
            } else {
                s.append(str);
            }
            s.append(' ');
        }
        return s.substring(0, s.length() - 1).toUpperCase();
    }

    public static String formatHexStr(String hexStr) {
        StringBuilder buf = new StringBuilder();
        for (int i = 0, c = 0; i < hexStr.length(); i = i + 32, c = c + 10) {
            String prefix= "0000" + c;
            buf.append(prefix.substring(prefix.length() - 4)).append('\t');
            buf.append(hexStr, i, i + 16).append(' ')
                .append(hexStr, i + 16, i + 31).append('\n');
        }
        return buf.toString();
    }

    public static void printClazz(Class<?> personClazz) {
    }
}
