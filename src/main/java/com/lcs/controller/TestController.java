package com.lcs.controller;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.lcs.entity.User;
import com.lcs.service.LoginService;
import com.lcs.util.MD5String;
import com.lcs.util.URLRequest;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.print.attribute.standard.JobSheets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

@RestController
public class TestController {
    @Resource
    private LoginService loginService;

    @RequestMapping("/ceshi")
    public User ceshi(@RequestBody User user){

        return loginService.getUserByName(user.getUsername());
    }
    @RequestMapping("/date")
    public void testDate(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar ca = Calendar.getInstance();
        ca.add(Calendar.DATE, 30);//独占时间为创建时间加30天
        try {
            Date enddate = dateFormat.parse(dateFormat.format(ca.getTime()));
            System.out.println(enddate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("/jiami")
    public void JiamiString(){

        String name = "Java程序员";
        String md5Name = MD5String.getMD5(name);

        System.out.println(md5Name);
    }
    @RequestMapping("/getStatements")
    public String getStatements() {
        String host="http://localhost:8082";
        String url="/yzbManager/interface/getStatements";
        String token="A8B3ABA78654B199087BE94584E989D8";
        //订单号
        String no = "cswsyh120190925001";
        //状态 1待内检 2待提交 3授权提交 4驳回 11待审核 12待发放 13审核中 21发放中 22部分完成 23标记完成 24完成
        Integer state = 1;
        //通道类型1银行卡 2支付宝
        Integer scid = 1;
        //商户号
        String merchantcode = "123456789";
        //提交时间
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.YEAR,2019);
        ca.set(Calendar.MONTH,01);
        ca.set(Calendar.DAY_OF_MONTH,11);
        Date submittime = ca.getTime();

        JSONObject json=new JSONObject();
        json.put("token",token);
        //json.put("no",no);
        json.put("state",state);
        //json.put("scid",scid);
        //json.put("merchantcode",merchantcode);
        //json.put("submittime",submittime);


        String result = URLRequest.httpPost(host + url, json);
        JSONObject jo = JSONObject.parseObject(result);
        System.out.println(json);
        return result;
    }


    @RequestMapping("/getBalance")
    public String getBalance() {
        String host="http://localhost:8082";
        String url="/yzbManager/interface/getBalance";
        String token="A8B3ABA78654B199087BE94584E989D8";

        String result = URLRequest.httpGet(host + url+"?token="+token);

        String str = new String("Welcome-to-Runoob");

        System.out.println("- 分隔符返回值 :" );
        for (String retval: str.split("-")){
            System.out.println(retval);
        }

        System.out.println("");
        System.out.println("- 分隔符设置分割份数返回值 :" );
        for (String retval: str.split("-", 1)){
            System.out.println(retval);
        }

        System.out.println("");
        String str2 = new String("www.runoob.com");
        System.out.println("转义字符返回值 :" );
        for (String retval: str2.split("\\.", 3)){
            System.out.println(retval);
        }

        System.out.println("");
        String str3 = new String("acount=? and uu =? or n=?");
        System.out.println("多个分隔符返回值 :" );
        for (String retval: str3.split("and|or")){
            System.out.println(retval);
        }

        //返回结果
        return result;
    }
}
