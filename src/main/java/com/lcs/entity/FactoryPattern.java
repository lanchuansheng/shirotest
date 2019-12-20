package com.lcs.entity;

import org.springframework.data.annotation.Id;

//工厂模式
public class FactoryPattern {
    public MathAlgorithm getAlgorithm(String type){
        if(type == null){
            return null;
        }else if (type.equals("add")){
            return new MathAdd();
        }else if (type.equals("substract")){
            return new MathSubstract();
        }else if (type.equals("multiply")){
            return new MathMultiply();
        }
        return null;
    }

    public static void main(String[] args) {
        FactoryPattern factoryPattern = new FactoryPattern();
        MathAlgorithm add = factoryPattern.getAlgorithm("add");
        System.out.println("5 + 10 = "+ add.calculate(5,10));
    }
}
