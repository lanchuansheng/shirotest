package com.lcs.service.impl;

import com.lcs.service.CallBackInterface;

public class Boss implements CallBackInterface {
    @Override
    public void execute() {
        System.out.println("活干完了"+System.currentTimeMillis());
    }
}
