package org.smart.framework.helper;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;

import org.smart.framework.annotation.Controller;
import org.smart.framework.annotation.Service;
import org.smart.framework.util.ClassUtil;

public final class ClassHelper {

	/**
	 * �����༯�ϣ����ڴ�������ص��ࣩ
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
	 * ��ȡӦ�ð��������е���
	 */
	public static Set<Class<?>> getClassSet(){
		return CLASS_SET;
	}
	
	/**
	 * ��ȡӦ�ð�����������Serviceע�����
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
	 * ��ȡӦ�ð�����������Controllerע�����
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
	 * ��ȡӦ�ð��������е�Bean�ࣨ������Service��Controller�ȣ�
	 */
	public static Set<Class<?>> getBeanClassSet(){
		
		Set<Class<?>> beanClassSet = new HashSet<Class<?>>();
		beanClassSet.addAll(getServiceClassSet());
		beanClassSet.addAll(getControllerClassSet());
		return beanClassSet;
	}
	
	/**
	 * ��ȡӦ�ð�����ĳ���ࣨ��ӿڣ����������ࣨ��ʵ���ࣩ
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
	 * ��ȡӦ�ð����´���ĳע���������
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
