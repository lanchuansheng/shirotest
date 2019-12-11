package com.lcs.util;



import java.text.SimpleDateFormat;
import java.util.Date;

public class OrderCodeUtils {

    public static String generateCode() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String timeStr = simpleDateFormat.format(new Date());
        StringBuffer sb = new StringBuffer(timeStr);
        int random =  (int)((Math.random()*9+1)*1000);
        sb.append(random);
        return sb.toString();
    }
}
