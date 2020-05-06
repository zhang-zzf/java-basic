package com.github.learn.basic.classinit;


/**
 * <p>1. 类加载 INSTANCE=null/c1=0/c2=0</p>
 * <p>2. 类初始化
 * <p>2.1 INSTANCE = new Singleton();c1=1;c2=1</p>
 * <p>2.2 c2=0</p>
 *
 * @author zhanfeng.zhang
 * @date 2020/05/06
 */
public class Singleton {

    public static final Singleton INSTANCE = new Singleton();
    public static int c1;
    public static int c2 = 0;


    public Singleton() {
        c1 += 1;
        c2 += 1;
    }

    public static Singleton getInstance() {
        return INSTANCE;
    }
}
