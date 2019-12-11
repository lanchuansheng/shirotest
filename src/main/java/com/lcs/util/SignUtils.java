package com.lcs.util;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by wcy on 2018/1/11.
 */


/**
 * 生成签名工具
 */
public class SignUtils {

    public static  String getGetSign(HttpServletRequest request, String token, String timestamp) {
        String url= SignUtils.getUrl(request);
        return SignUtils.generSign(token, timestamp, url);
    }
    public static  String getPostSign(HttpServletRequest request, String token, String timestamp) {
        StringBuffer url=request.getRequestURL();
        return SignUtils.generSign(token, timestamp, url.toString());
    }

    public static String getUrl(HttpServletRequest request){
        StringBuffer uri = request.getRequestURL();
        String url = uri.toString();
        return url;
    }

    public static String generSign(String token,String timestamp,String url){
        String sign=token+timestamp;
        return new MD5Utils().string2MD5(sign).toLowerCase();
    }
}
