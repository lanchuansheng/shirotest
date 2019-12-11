package com.lcs.util;

import app.yzb.entity.UserInfo;
import app.yzb.exceptions.GlobleException;
import com.alibaba.fastjson.JSON;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

public class JwtUtils {

    public static String JwtSecret = "shandianxia";

    /**
     * 创建一个用户的token
     *
     * @param user
     * @return
     */
    public static String createuser(UserInfo user) {
        if (user == null)
            throw new GlobleException("user不能为null");
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        return Jwts.builder().setId("1") // 版本
                .setIssuer("发行人") // 发行人
                .setAudience("user") // 角色
                .setIssuedAt(now) // 发行时间
				.setSubject(JSON.toJSONString(user)).signWith(signatureAlgorithm, JwtSecret).compact();

    }

    public static UserInfo parseUser(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(JwtSecret)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            throw new GlobleException("请重新登录！");
        }
        UserInfo user = JSON.parseObject(claims.getSubject(), UserInfo.class);
        return user;
    }
}
