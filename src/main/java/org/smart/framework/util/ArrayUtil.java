package org.smart.framework.util;

import org.apache.commons.lang3.ArrayUtils;

/**
 * ���鹤��
 * @author Administrator
 *
 */
public class ArrayUtil {

	/**
	 * �ж������Ƿ�Ϊ��
	 */
	public static boolean isEmpty(Object[] array){
		
		return ArrayUtils.isEmpty(array);
	}
	
	/**
	 * �ж������Ƿ�ǿ�
	 */
	public static boolean isNotEmpty(Object[] array){
		
		return !ArrayUtils.isEmpty(array);
	}
}
