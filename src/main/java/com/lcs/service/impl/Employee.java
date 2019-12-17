package com.lcs.service.impl;

import com.lcs.service.CallBackInterface;

public class Employee {
    private CallBackInterface callBackInterface = null;

    public void setCallBackInterface(CallBackInterface callBackInterface){
        this.callBackInterface =callBackInterface;
    }
    public void doSome(){

        //1.开始干活了
        for(int i=0;i<10;i++){
            System.out.println("第【" + i + "】事情干完了！");
        }

        //2.告诉老板干完了
        callBackInterface.execute();
    }

}
