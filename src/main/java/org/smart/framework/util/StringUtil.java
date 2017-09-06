package org.smart.framework.util;

import org.apache.commons.lang3.StringUtils;

/**
 * 字符串工具
 * @author Administrator
 *
 */
public final class StringUtil {

	/**
	 * 判断字符串是否为空
	 */
	public static boolean isEmpty(String str){
		if(str != null){
			str = str.trim();
		}
		return StringUtils.isEmpty(str);
	}
	
	/**
	 * 判断字符串是否非空
	 */
	public static boolean isNotEmpty(String str){
		return !isEmpty(str);
	}
	
	/**
	 * 按照指定字符对字符串进行分解
	 */
	public static String[] splitString(String str, String splitStr){
		
		String[] strArr;
		strArr = str.split(splitStr);
		return strArr;
	}
}
