package org.smart.framework.helper;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart.framework.annotation.Aspect;
import org.smart.framework.proxy.AspectProxy;
import org.smart.framework.proxy.Proxy;
import org.smart.framework.proxy.ProxyManager;

public final class AopHelper {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(AopHelper.class);
	
	static{
		System.out.println("AopHelper is loading...");
		try {
			//代理类和目标集合类映射关系（一对多）
			Map<Class<?>, Set<Class<?>>> proxyMap = createProxyMap();
			//目标类和代理对象映射关系（一对多）
			Map<Class<?>, List<Proxy>> targetMap = createTargetMap(proxyMap);
			//纵向遍历所有目标类和代理对象
			for (Map.Entry<Class<?>, List<Proxy>> targetEntry : targetMap.entrySet()) {
				Class<?> targetClass = targetEntry.getKey();
				List<Proxy> proxyList = targetEntry.getValue();
				Object proxy = ProxyManager.createProxy(targetClass, proxyList);//循环创建多个代理对象(一个目标类对应多个代理)(第一次循环在这里)
				BeanHelper.setBean(targetClass, proxy);
			}
			
		} catch (Exception e) {
			LOGGER.error("aop load failure", e);
		}
	}

	/**
	 * 获取带有Aspect注解的所有类（被增强类）
	 */
	private static Set<Class<?>> createTargetClassSet(Aspect aspect) throws Exception{
		
		Set<Class<?>> targetClassSet = new HashSet<Class<?>>();
		Class<? extends Annotation> annotation = aspect.value();//Controller.class
		if(annotation != null && !annotation.equals(Aspect.class)){
			targetClassSet.addAll(ClassHelper.getClassSetByAnnotation(annotation));//应用包名下所有使用某注解的所有类（这里Controller只有一个类）
		}
		return targetClassSet;
		
		
//		Set<Class<?>> targetClassSet = new HashSet<Class<?>>();
//		String value = aspect.value();
//		if(value.contains(":")){
//			String[] array1 = value.split(":");
//			if(array1[1].contains("/")){
//				String[] array2 = array1[1].split("/");
//				
//			}
//		}
	}
	
	/**
	 * 获取代理类和目标类集合之间的映射关系（一对多）
	 * 代理类需要扩展AspectProxy抽象类，还需要带有Aspect注解，只有满足这两个条件，才能根据Aspect注解
	 * 所定义的注解属性去解析对应的目标类集合，然后才能建立代理类与目标类集合之间的映射关系。
	 */
	private static Map<Class<?>, Set<Class<?>>> createProxyMap() throws Exception{
		
		Map<Class<?>, Set<Class<?>>> proxyMap = new HashMap<Class<?>, Set<Class<?>>>();
		Set<Class<?>> proxyClassSet = ClassHelper.getClassSetBySuper(AspectProxy.class);//这里的方法是否该在ClassUtil类内，而不是ClassHelper类内？
		for (Class<?> proxyClass : proxyClassSet) {//proxyClass为ControllerAspect
			if(proxyClass.isAnnotationPresent(Aspect.class)){
				Aspect aspect = proxyClass.getAnnotation(Aspect.class);//?
				Set<Class<?>> targetClassSet = createTargetClassSet(aspect);
				proxyMap.put(proxyClass, targetClassSet);//这里得到了ControllerAspect和应用包名下所有使用Controller注解的所有类（这里只有Controller一个类）的映射关系
			}
		}
		return proxyMap;
	}
	
	/**
	 * 一旦获取的代理类与目标类集合之间的映射关系，就能根据这个关系分析出目标类与代理对象列表之间的映射关系
	 */
	private static Map<Class<?>, List<Proxy>> createTargetMap(Map<Class<?>, Set<Class<?>>> proxyMap) throws Exception{
		
		Map<Class<?>, List<Proxy>> targetMap = new HashMap<Class<?>, List<Proxy>>();
		for (Map.Entry<Class<?>, Set<Class<?>>> proxyEntry : proxyMap.entrySet()) {
			
			Class<?> proxyClass = proxyEntry.getKey();
			Set<Class<?>> targetClassSet = proxyEntry.getValue();
			for (Class<?> targetClass : targetClassSet) {
				Proxy proxy = (Proxy) proxyClass.newInstance();
				if(targetMap.containsKey(targetClass)){
					targetMap.get(targetClass).add(proxy);
				}else{
					List<Proxy> proxyList = new ArrayList<Proxy>();
					proxyList.add(proxy);
					targetMap.put(targetClass, proxyList);
				}
			}
		}
		return targetMap;
	}
}
