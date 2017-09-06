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
			//�������Ŀ�꼯����ӳ���ϵ��һ�Զࣩ
			Map<Class<?>, Set<Class<?>>> proxyMap = createProxyMap();
			//Ŀ����ʹ������ӳ���ϵ��һ�Զࣩ
			Map<Class<?>, List<Proxy>> targetMap = createTargetMap(proxyMap);
			//�����������Ŀ����ʹ������
			for (Map.Entry<Class<?>, List<Proxy>> targetEntry : targetMap.entrySet()) {
				Class<?> targetClass = targetEntry.getKey();
				List<Proxy> proxyList = targetEntry.getValue();
				Object proxy = ProxyManager.createProxy(targetClass, proxyList);//ѭ����������������(һ��Ŀ�����Ӧ�������)(��һ��ѭ��������)
				BeanHelper.setBean(targetClass, proxy);
			}
			
		} catch (Exception e) {
			LOGGER.error("aop load failure", e);
		}
	}

	/**
	 * ��ȡ����Aspectע��������ࣨ����ǿ�ࣩ
	 */
	private static Set<Class<?>> createTargetClassSet(Aspect aspect) throws Exception{
		
		Set<Class<?>> targetClassSet = new HashSet<Class<?>>();
		Class<? extends Annotation> annotation = aspect.value();//Controller.class
		if(annotation != null && !annotation.equals(Aspect.class)){
			targetClassSet.addAll(ClassHelper.getClassSetByAnnotation(annotation));//Ӧ�ð���������ʹ��ĳע��������ࣨ����Controllerֻ��һ���ࣩ
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
	 * ��ȡ�������Ŀ���༯��֮���ӳ���ϵ��һ�Զࣩ
	 * ��������Ҫ��չAspectProxy�����࣬����Ҫ����Aspectע�⣬ֻ���������������������ܸ���Aspectע��
	 * �������ע������ȥ������Ӧ��Ŀ���༯�ϣ�Ȼ����ܽ�����������Ŀ���༯��֮���ӳ���ϵ��
	 */
	private static Map<Class<?>, Set<Class<?>>> createProxyMap() throws Exception{
		
		Map<Class<?>, Set<Class<?>>> proxyMap = new HashMap<Class<?>, Set<Class<?>>>();
		Set<Class<?>> proxyClassSet = ClassHelper.getClassSetBySuper(AspectProxy.class);//����ķ����Ƿ����ClassUtil���ڣ�������ClassHelper���ڣ�
		for (Class<?> proxyClass : proxyClassSet) {//proxyClassΪControllerAspect
			if(proxyClass.isAnnotationPresent(Aspect.class)){
				Aspect aspect = proxyClass.getAnnotation(Aspect.class);//?
				Set<Class<?>> targetClassSet = createTargetClassSet(aspect);
				proxyMap.put(proxyClass, targetClassSet);//����õ���ControllerAspect��Ӧ�ð���������ʹ��Controllerע��������ࣨ����ֻ��Controllerһ���ࣩ��ӳ���ϵ
			}
		}
		return proxyMap;
	}
	
	/**
	 * һ����ȡ�Ĵ�������Ŀ���༯��֮���ӳ���ϵ�����ܸ��������ϵ������Ŀ�������������б�֮���ӳ���ϵ
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
