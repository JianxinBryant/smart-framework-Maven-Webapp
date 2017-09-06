package org.smart.framework.bean;

import java.util.Map;

import org.smart.framework.util.CastUtil;

/**
 * �����������
 * @author Administrator
 *
 */
public class Param {

	private Map<String, Object> paramMap;
	
	public Param(Map<String, Object> paramMap){
		
		this.paramMap = paramMap;
	}
	
	/**
	 * ���ݲ�������ȡlong�Ͳ���ֵ
	 */
	public Long getLong(String name){
		
		return CastUtil.castLong(paramMap.get(name));
	}
	
	/**
	 * ��ȡ�����ֶ���Ϣ
	 */
	public Map<String, Object> getMap(){
		
		return paramMap;
	}
	
}
