package org.smart.framework.util;

import org.apache.commons.lang3.StringUtils;

/**
 * �ַ�������
 * @author Administrator
 *
 */
public final class StringUtil {

	/**
	 * �ж��ַ����Ƿ�Ϊ��
	 */
	public static boolean isEmpty(String str){
		if(str != null){
			str = str.trim();
		}
		return StringUtils.isEmpty(str);
	}
	
	/**
	 * �ж��ַ����Ƿ�ǿ�
	 */
	public static boolean isNotEmpty(String str){
		return !isEmpty(str);
	}
	
	/**
	 * ����ָ���ַ����ַ������зֽ�
	 */
	public static String[] splitString(String str, String splitStr){
		
		String[] strArr;
		strArr = str.split(splitStr);
		return strArr;
	}
}
