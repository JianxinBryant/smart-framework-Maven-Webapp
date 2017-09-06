package org.smart.framework.helper;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

import org.smart.framework.annotation.Controller;
import org.smart.framework.annotation.Service;
import org.smart.framework.util.ClassUtil;

public final class ClassHelper {

	/**
	 * 定义类集合（用于存放所加载的类）
	 */
	private static final Set<Class<?>> CLASS_SET;
	
	static{
		System.out.println("ClassHelper is loading...");
		String basePackage = ConfigHelper.getAppBasePackage();
		System.out.println(basePackage);
		CLASS_SET = ClassUtil.getClassSet(basePackage);
		System.out.println("CLASS_SET = " + CLASS_SET);
	}
	
	/**
	 * 获取应用包名下所有的类
	 */
	public static Set<Class<?>> getClassSet(){
		return CLASS_SET;
	}
	
	/**
	 * 获取应用包名下所有有Service注解的类
	 */
	public static Set<Class<?>> getServiceClassSet(){
		Set<Class<?>> classSet = new HashSet<Class<?>>();
		for (Class<?> cls : CLASS_SET) {
			if(cls.isAnnotationPresent(Service.class)){
				classSet.add(cls);
			}
		}
		return classSet;
	} 
	
	/**
	 * 获取应用包名下所有有Controller注解的类
	 */
	public static Set<Class<?>> getControllerClassSet(){
		Set<Class<?>> classSet = new HashSet<Class<?>>();
		for (Class<?> cls : CLASS_SET) {
			if(cls.isAnnotationPresent(Controller.class)){
				classSet.add(cls);
			}
		}
		return classSet;
		
	}
	
	
	/**
	 * 获取应用包名下所有的Bean类（包括：Service、Controller等）
	 */
	public static Set<Class<?>> getBeanClassSet(){
		
		Set<Class<?>> beanClassSet = new HashSet<Class<?>>();
		beanClassSet.addAll(getServiceClassSet());
		beanClassSet.addAll(getControllerClassSet());
		return beanClassSet;
	}
	
	/**
	 * 获取应用包名下某父类（或接口）的所有子类（或实现类）
	 */
	public static Set<Class<?>> getClassSetBySuper(Class<?> superClass){
		
		Set<Class<?>> classSet = new HashSet<Class<?>>();
		for (Class<?> cls : CLASS_SET) {
			if(superClass.isAssignableFrom(cls) && !superClass.equals(cls)){
				
				classSet.add(cls);
			}
		}
		return classSet;
	}
	
	/**
	 * 获取应用包名下带有某注解的所有类
	 */
	public static Set<Class<?>> getClassSetByAnnotation(Class<? extends Annotation> annotationClass){
		
		Set<Class<?>> classSet = new HashSet<Class<?>>();
		for (Class<?> cls : CLASS_SET) {
			if(cls.isAnnotationPresent(annotationClass)){
				classSet.add(cls);
			}
		}
		return classSet;
	}
}
