package org.smart.framework.helper;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.smart.framework.util.ReflectionUtil;
/**
 * Bean助手类
 * @author Administrator
 *
 */
public final class BeanHelper {

	/**
	 * 定义Bean映射(用于存放Bean类与Bean实例的映射关系)
	 * 通过Map的映射关系，去构造一个“类”（这里的bean类是指应用包名下所有的Bean类）
	 * 与自身的实例的映射关系（构建这层关系说白了就是循环挨个实例），这样就可以通过传类名就能得到里面所有的实例
	 * 这样就封装了实例这一块（封装涉及到所有的bean）。
	 * BeanHelper有了下面这些方法过后，就相当于一个“bean容器”了，
	 */
	private static final Map<Class<?>, Object> BEAN_MAP = new HashMap<Class<?>, Object>();
	
	static{
		System.out.println("BeanHelper is loading...");
		//这里ClassHelper调用的getBeanClassSet方法返回的是 应用包名下的带有Controller和Service注解的Bean类
		Set<Class<?>> beanClassSet = ClassHelper.getBeanClassSet();
		for (Class<?> beanClass : beanClassSet) {
			Object obj = ReflectionUtil.newInstance(beanClass);
			//beanClass:Controller、Service类，obj:controller、service实例
			BEAN_MAP.put(beanClass, obj);
		}
		System.out.println(BEAN_MAP);
	}
	
	/**
	 * 获取Bean映射
	 */
	public static Map<Class<?>, Object> getBeanMap(){
		return BEAN_MAP;
	}
	
	/**
	 * 获取Bean实例
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getBean(Class<T> beanClass){
		
		if(!BEAN_MAP.containsKey(beanClass)){
			throw new RuntimeException("can not get bean by class:" + beanClass);
		}
		return (T) BEAN_MAP.get(beanClass);
	}
	
	/**
	 * 设置Bean实例
	 */
	public static void setBean(Class<?> cls, Object obj){
		
		BEAN_MAP.put(cls, obj);
	}
}
