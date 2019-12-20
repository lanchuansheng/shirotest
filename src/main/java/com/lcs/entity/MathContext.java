package com.lcs.entity;

public class MathContext {
    private MathAlgorithm mathAlgorithm;
    public MathContext(MathAlgorithm mathAlgorithm){
        this.mathAlgorithm = mathAlgorithm;
    }

    public Integer mathExcute(Integer num1, Integer num2){
        return mathAlgorithm.calculate(num1,num2);

    }



}
