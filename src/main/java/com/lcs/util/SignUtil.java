package com.lcs.util;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.*;


/**
 * 
 * @ClassName: SignUtil 
 * @Description: TODO(签名加密工具类) 
 * @author Young Meow
 * @date 2016年9月1日
 */
public class SignUtil {
 
    private final static String SING = "ZHZR";
    private final static String input_charset = "UTF-8";
    private static final Logger LOG = LogManager.getLogger(SignUtil.class);
 
    public static void main(String[] args) throws Exception {
    	SortedMap<String, String> finalpackage = new TreeMap<String, String>();
    	finalpackage.put("ALIPAYCODE", "SUCCESS"); 
    	finalpackage.put("ORDERID", "12824"); 
    	finalpackage.put("PAYTYPE", "5"); 
    	finalpackage.put("PRICE", "0.01"); 
    	finalpackage.put("TOTALPRICE", "0.01"); 
    	Integer couponType = null;
    	System.out.println(couponType+"");
		System.out.println("加密后："+signMD5(finalpackage));
    	
        
    }
    
    
    
    
     
    /**
     * Description 根据键值进行加密
     * @param
     * @param
     * @return
     * @throws Exception
     */
    public static String signMD5(SortedMap<String, String> sParaTemp){
	    String strsign = createLinkString(sParaTemp);
	    strsign="SING="+SING+"&"+strsign;
	    strsign=strsign.toUpperCase();
        LOG.info(strsign);
	    String strs=MD5Util.MD5Encode(strsign,input_charset);
        LOG.info(strs);
        return strs;
    }
 


    /** 
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
     * @param params 需要排序并参与字符拼接的参数组
     * @return 拼接后字符串
     */
    public static String createLinkString(SortedMap<String, String> params) {

        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);

        String prestr = "";

        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);

            if (i == keys.size() - 1) {//拼接时，不包括最后一个&字符
                prestr = prestr + key + "=" + value;
            } else {
                prestr = prestr + key + "=" + value + "&";
            }
        }

        return prestr;
    }
     
   
    
   
	
    
}