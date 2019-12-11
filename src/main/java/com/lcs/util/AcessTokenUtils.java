package com.lcs.util;

/**
 * Created by wcy on 2018/1/11.
 */


/**
 *生成token工具
 */
public class AcessTokenUtils {

    public  static String getAcessToken(String account,String password){
        String token=account+password+System.currentTimeMillis();
        return new MD5Utils().string2MD5(token);
    }

}
