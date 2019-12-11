package com.lcs.util;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

/**
 * 校验工具类
 * @author liuns
 *
 */
public class ValidUtils {
	/**
	 * 从校验错误中获取校验信息
	 * @param BindingResult result
	 * @return
	 */
	public static String getErrorMessage(BindingResult result){
		StringBuffer sb = new StringBuffer();  
        for(ObjectError objectError: result.getAllErrors()){
            sb.append(((FieldError)objectError).getField() +": ").append(objectError.getDefaultMessage()).append(";");
        }
        return sb.toString();
	}
}
