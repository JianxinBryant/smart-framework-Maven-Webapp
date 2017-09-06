package org.smart.framework.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * ���乤����
 * @author Administrator
 *
 */
public class ReflectionUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(ReflectionUtil.class);
	
	/**
	 * ����ʵ��
	 */
	public static Object newInstance(Class<?> cls){
		
		Object instance;
		try {
			instance = cls.newInstance();
		} catch (Exception e) {
			LOGGER.error("new instance failure", e);
			throw new RuntimeException(e);
		}
		
		return instance;
	}
	
	/**
	 * ���÷���
	 */
	public static Object invokeMethod(Object obj, Method method, Object...args){
		
		Object result;
		method.setAccessible(true);
		try {
			result = method.invoke(obj, args);
		} catch (Exception e) {
			LOGGER.error("method invoke failure", e);
			throw new RuntimeException();
		}
		return result;
	} 
	
	/**
	 * ���ó�Ա������ֵ
	 */
	public static void setField(Object obj, Object value, Field field){
		
		field.setAccessible(true);
		try {
			field.set(obj, value);
		} catch (Exception e) {
			LOGGER.error("set field failure", e);
			throw new RuntimeException(e);
		}
	}
	
	
	
	
}
