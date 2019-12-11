package com.lcs.util;

import com.lcs.enums.DictEnum;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by wcy on 2018/2/24.
 */
public class EnumUtils {


    /**
     * 根据字典枚举的类型和值取出标签
     * @param value
     * @param type
     * @return
     */
    public static String getLable(String value,String type){
        for(DictEnum en : DictEnum.values()){
            if(StringUtils.equals(value, en.getValue()) && StringUtils.equals(type, en.getType())){
                return en.getLable();
            }
        }
        return null;
    }
}
