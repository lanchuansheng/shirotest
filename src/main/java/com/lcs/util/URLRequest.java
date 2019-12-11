package com.lcs.util;


import com.alibaba.fastjson.JSONObject;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by wcy on 2017/11/14.
 */
public class URLRequest {
    private static final Logger logger = LoggerFactory.getLogger(URLRequest.class);

    /**
     * 发起get请求
     *
     * @param url
     * @return
     */
    public static String httpGet(String url) {
        String result = null;
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(url);
        BufferedReader br = null;
        InputStream in = null;
        try {
            httpGet.addHeader("Content-Type", "application/json");
            CloseableHttpResponse response = httpclient.execute(httpGet);
            StringBuilder sb = new StringBuilder();
            in = response.getEntity().getContent();
            br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            result = sb.toString();
            logger.info(result);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return result;
    }

    /**
     * 发送httppost请求
     *
     * @return
     */
    public static String httpPost(String url, JSONObject json) {
        logger.info(json.toJSONString());
        String result = null;
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        BufferedReader br = null;
        InputStream in = null;
        try {
            httpPost.addHeader("Content-Type", "application/json");
            if (json != null) {
                StringEntity entity = new StringEntity(json.toString(), "utf-8");//解决中文乱码问题
                entity.setContentEncoding("UTF-8");
                entity.setContentType("application/json");
                httpPost.setEntity(entity);

            }
            CloseableHttpResponse response = httpclient.execute(httpPost);
            StringBuilder sb = new StringBuilder();
            in = response.getEntity().getContent();
            br = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            result = sb.toString();
            logger.info(result);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
        return result;
    }
}
