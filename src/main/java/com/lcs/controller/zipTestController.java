package com.lcs.controller;

import sun.net.ftp.impl.FtpClient;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class zipTestController {
    public static void main(String[] args) {
        SimpleDateFormat dfs = new SimpleDateFormat("yyyyMMddHHmmss");
        String format = dfs.format(new Date());
        Random r = new Random();
        System.out.println(format+(r.nextInt(900000)+100000));


    }
}
