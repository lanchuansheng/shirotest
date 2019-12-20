package com.lcs.entity;
//策略模式
public class StrategyPattern {

    public static void main(String[] args) {
        MathContext context = new MathContext(new MathAdd());
        System.out.println("10 + 5 = " + context.mathExcute(10, 5));

        context = new MathContext(new MathSubstract());
        System.out.println("10 - 5 = " + context.mathExcute(10, 5));

        context = new MathContext(new MathMultiply());
        System.out.println("10 * 5 = " + context.mathExcute(10, 5));


    }
}
