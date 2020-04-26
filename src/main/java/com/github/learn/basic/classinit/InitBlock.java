package com.github.learn.basic.classinit; /**
 *
 */

/**
 * @author zhangzhanfeng
 * @date Aug 25, 2017
 */
public class InitBlock {

    private double weight;

    {
        weight = 3.0;
    }

    {
        System.out.println(weight);
        weight = 4.0;
        System.out.println(weight);
    }

    public InitBlock() {
        System.out.println(weight);
    }

    public static void main(String[] args) {
        InitBlock p = new InitBlock();
    }
}
