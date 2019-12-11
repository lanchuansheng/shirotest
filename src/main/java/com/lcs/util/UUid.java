package com.lcs.util;

import java.util.UUID;

/**
 * Created by wcy on 2017/10/25.
 */
public class UUid {
    public static String uuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static String uuidBig() {
        return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
    }

    public static void main(String[] args) {
       String uuid =  uuid();
        String uuidbig =  uuidBig();
        System.out.println(uuid);
       System.out.println(uuidbig);
    }
}
