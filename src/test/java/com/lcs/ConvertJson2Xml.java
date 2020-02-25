package com.lcs;


import ch.qos.logback.core.joran.util.beans.BeanUtil;
import com.lcs.entity.Role;
import com.lcs.entity.User;
import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;
import org.springframework.beans.BeanUtils;

import javax.activation.MailcapCommandMap;
import javax.lang.model.element.VariableElement;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

public class ConvertJson2Xml {

    public static JSONObject json2Xml(){
        User stu = User.getInstance();
        stu.setUsername("zhangsan");
        stu.setPassword("123456");
        stu.setId(1L);
        //String json = JSONObject.(stu);
        JSONObject jsonObject = JSONObject.fromObject(stu);
        return jsonObject;
    }


    public static String jsonToXML(JSONObject json) {
       /* System.out.println("json字符串转xml字符串");
        String jsonStr = "{\"password\":\"\",\"username\":\"张三\"}";
        JSONObject json = JSONObject.fromObject(jsonStr);*/
        XMLSerializer xmlSerializer = new XMLSerializer();
        xmlSerializer.setRootName("user");
        xmlSerializer.setTypeHintsEnabled(false);
        String xml = xmlSerializer.write(json);
        System.out.println("json--->xml \n" + xml);
        return xml;
    }


    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        list.add("我是");
        list.add("飞机上刊登");
        list.add("第三方");
        list.add("飞机客服");
        list.add("飞机客");
        List<String> list1 =new ArrayList<>();
        System.out.println("abc".equals("abc"));
        HashSet<String> hashSet = new HashSet<>(list);
        if(list.size() != hashSet.size()){
            System.out.println("list中存在重复的数据");
        }
        for (String s : hashSet) {
            System.out.println(s);
        }


        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH时mm分ss秒");
        LocalDateTime of = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        //System.out.println(date);
        Date date = Date.from(of.atZone(ZoneId.systemDefault()).toInstant());
        of.atZone(ZoneId.systemDefault()).toInstant();
        System.out.println(date);

    }
}
