package org.smart.framework.helper;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.smart.framework.util.ReflectionUtil;
/**
 * Bean������
 * @author Administrator
 *
 */
public final class BeanHelper {

	/**
	 * ����Beanӳ��(���ڴ��Bean����Beanʵ����ӳ���ϵ)
	 * ͨ��Map��ӳ���ϵ��ȥ����һ�����ࡱ�������bean����ָӦ�ð��������е�Bean�ࣩ
	 * �������ʵ����ӳ���ϵ����������ϵ˵���˾���ѭ������ʵ�����������Ϳ���ͨ�����������ܵõ��������е�ʵ��
	 * �����ͷ�װ��ʵ����һ�飨��װ�漰�����е�bean����
	 * BeanHelper����������Щ�������󣬾��൱��һ����bean�������ˣ�
	 */
	private static final Map<Class<?>, Object> BEAN_MAP = new HashMap<Class<?>, Object>();
	
	static{
		System.out.println("BeanHelper is loading...");
		//����ClassHelper���õ�getBeanClassSet�������ص��� Ӧ�ð����µĴ���Controller��Serviceע���Bean��
		Set<Class<?>> beanClassSet = ClassHelper.getBeanClassSet();
		for (Class<?> beanClass : beanClassSet) {
			Object obj = ReflectionUtil.newInstance(beanClass);
			//beanClass:Controller��Service�࣬obj:controller��serviceʵ��
			BEAN_MAP.put(beanClass, obj);
		}
		System.out.println(BEAN_MAP);
	}
	
	/**
	 * ��ȡBeanӳ��
	 */
	public static Map<Class<?>, Object> getBeanMap(){
		return BEAN_MAP;
	}
	
	/**
	 * ��ȡBeanʵ��
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getBean(Class<T> beanClass){
		
		if(!BEAN_MAP.containsKey(beanClass)){
			throw new RuntimeException("can not get bean by class:" + beanClass);
		}
		return (T) BEAN_MAP.get(beanClass);
	}
	
	/**
	 * ����Beanʵ��
	 */
	public static void setBean(Class<?> cls, Object obj){
		
		BEAN_MAP.put(cls, obj);
	}
}
