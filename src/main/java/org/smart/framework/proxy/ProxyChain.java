package org.smart.framework.proxy;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import net.sf.cglib.proxy.MethodProxy;

/**
 * ������
 * @author Administrator
 *
 */
public class ProxyChain {

	/**
	 * Ŀ����
	 */
	private final Class<?> targetClass;
	
	/**
	 * Ŀ�����
	 */
	private final Object targetObject;
	
	/**
	 * Ŀ�귽��
	 */
	private final Method targetMethod;
	
	/**
	 * ��������
	 */
	private final MethodProxy methodProxy;
	
	/**
	 * ��������
	 */
	private final Object[] methodParams;
	
	/**
	 * �����б�
	 */
	private List<Proxy> proxyList = new ArrayList<Proxy>();
	
	/**
	 * ��������
	 */
	private int proxyIndex = 0;
	
	public ProxyChain(Class<?> targetClass, Object targetObject, Method targetMethod,
			MethodProxy methodProxy, Object[] methodParams, List<Proxy> proxyList){
		
		this.targetClass = targetClass;
		this.targetObject = targetObject;
		this.targetMethod = targetMethod;
		this.methodProxy = methodProxy;
		this.methodParams = methodParams;
		this.proxyList = proxyList;
	}

	public Class<?> getTargetClass() {
		return targetClass;
	}
	
	public Method getTargetMethod() {
		return targetMethod;
	}

	public Object[] getMethodParams() {
		return methodParams;
	}
	
	/**
	 * ��ProxyManager����proxy����Ĺ����У�ִ����Proxy�ӿڵ�ʵ�����doProxy����
	 * @return
	 * @throws Throwable
	 */
	public Object doProxyChain()throws Throwable{
		
		Object methodResult;
		if(proxyIndex < proxyList.size()){
			
			methodResult = proxyList.get(proxyIndex++).doProxy(this);
		}else{//�������ӿ�ʵ����ִ����Ϻ�eg��ControllerAspect�̳г�����AspectProxy��
			
			methodResult = methodProxy.invokeSuper(targetObject, methodParams);
		}
		return methodResult;
	}
}
