package org.smart.framework.proxy;

import java.lang.reflect.Method;
import java.util.List;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * 代理管理器：他提供了一个创建代理对象的方法，输入一个目标类和一组Proxy接口实现，输出一个代理对象。
 * 代理管理器功能就是创建所有代理对象
 * @author Administrator
 *
 */
public class ProxyManager {

	@SuppressWarnings("unchecked")
	public static <T> T createProxy(final Class<?> targetClass, final List<Proxy> proxyList){
		
		return (T) Enhancer.create(targetClass, new MethodInterceptor(){

			
			public Object intercept(Object targetObject, Method targetMethod, Object[] methodParams,
					MethodProxy methodProxy) throws Throwable {
				
				return new ProxyChain(targetClass, targetObject, targetMethod, methodProxy, methodParams, proxyList).doProxyChain();
			}
		});
	}


}
