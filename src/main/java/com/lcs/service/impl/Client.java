package com.lcs.service.impl;

import com.lcs.entity.User;

import javax.jws.soap.SOAPBinding;

public class Client {

    public static void main(String[] args) {

        Employee emp = new Employee();

        //将回调对象（上层对象）传入，注册
        emp.setCallBackInterface(new Boss());

        //开启控制器对象运行
        emp.doSome();
        User user = User.getInstance();
        user.showMessage();
        System.out.println(user);
        System.out.println("---------------------");
        User u = User.getInstance();
        System.out.println(u);


    }

}