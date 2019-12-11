package com.lcs.util;

import app.yzb.submail.config.AppConfig;
import app.yzb.submail.lib.MESSAGEXsend;
import app.yzb.submail.utils.ConfigLoader;

import java.util.Random;

/**
 * Created by lowdad on 18-1-23.
 */
public class SmsUtil {
    public static String SendMessages(String account) {
        AppConfig config = ConfigLoader.load(ConfigLoader.ConfigType.Message);
        MESSAGEXsend submail = new MESSAGEXsend(config);
        String smscode = getRandNum(6);
        submail.addTo(account);
        submail.setProject("JNLqU");
        submail.addVar("zhzr", "" + smscode + "");
        submail.xsend();
        return smscode;
    }
    public static void SendJSDMessages(String account,String content) {
        AppConfig config = ConfigLoader.load(ConfigLoader.ConfigType.Message);
        MESSAGEXsend submail = new MESSAGEXsend(config);
        submail.addTo(account);
        submail.setProject("5eCaW4");
        submail.addVar("zhzr", "" + content + "");
        submail.xsend();
    }

    public static void SendChongZhiMessages(String account,String content) {
        AppConfig config = ConfigLoader.load(ConfigLoader.ConfigType.Message);
        MESSAGEXsend submail = new MESSAGEXsend(config);
        submail.addTo(account);
        submail.setProject("pdtNN4");
        submail.addVar("zhzr", "" + content + "");
        submail.xsend();
    }
    public static String getRandNum(int charCount) {
        String charValue = "";
        for (int i = 0; i < charCount; i++) {
            char c = (char) (randomInt(0, 10) + '0');
            charValue += String.valueOf(c);
        }
        return charValue;
    }

    public static int randomInt(int from, int to) {
        Random r = new Random();
        return from + r.nextInt(to - from);
    }

    public static void  main(String[] args){
        SmsUtil.SendJSDMessages("13964852432", "1111光棍节");
    }
}
