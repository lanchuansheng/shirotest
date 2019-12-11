package com.lcs.util;

import org.apache.shiro.crypto.hash.SimpleHash;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by wcy on 2017/10/26.
 */
public class MD5Utils {
    /**
     * MD5加密
     *
     * @param data 明文字符串
     * @return 16进制密文
     */
    public  String string2MD5(String data) {
        return encryptMD5ToString(data.getBytes());
    }


    static final char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    public  String encryptMD5ToString(byte[] data) {
        return bytes2HexString(encryptMD5(data));
    }
    public  byte[] encryptMD5(byte[] data) {
        return hashTemplate(data, "MD5");
    }

    /**
     * hash加密模板
     *
     * @param data      数据
     * @param algorithm 加密算法
     * @return 密文字节数组
     */
    private  byte[] hashTemplate(byte[] data, String algorithm) {
        if (data == null || data.length <= 0) {
            return null;
        }
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            md.update(data);
            return md.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }
    public  String bytes2HexString(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        int len = bytes.length;
        if (len <= 0) {
            return null;
        }
        char[] ret = new char[len << 1];
        for (int i = 0, j = 0; i < len; i++) {
            ret[j++] = hexDigits[bytes[i] >>> 4 & 0x0f];
            ret[j++] = hexDigits[bytes[i] & 0x0f];
        }
        return new String(ret);
    }


    public static Object saltMD5(Object password,Object salt,int hashIterations){
        String hashAlgorithmName="MD5";
        Object result = new SimpleHash(hashAlgorithmName,password,salt,hashIterations);
        return result;
    }



}

