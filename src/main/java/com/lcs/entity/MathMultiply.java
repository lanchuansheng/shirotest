package com.lcs.entity;

public class MathMultiply implements MathAlgorithm{

    @Override
    public Integer calculate(Integer num1, Integer num2) {
        return num1*num2;
    }
}
