package com.lcs.util;

/**
 * Excel 类型，根据后缀名不同可分为03和07，两种类型解析方式不同
 * @author liuns 2017年5月8日上午8:53:36
 *
 */
public enum ExcelType {
	/**
	 * 后缀是xls的
	 */
	EXCEL03,
	/**
	 * 后缀是xlsx的
	 */
	EXCEL07,
	/**
	 * 未知
	 */
	UNKNOWN
}
