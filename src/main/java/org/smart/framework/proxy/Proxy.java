package org.smart.framework.proxy;
/**
 * ����ӿ�
 * @author Administrator
 *
 */
public interface Proxy {

	/**
	 * ִ����ʽ����
	 */
	Object doProxy(ProxyChain proxyChain) throws Throwable;
}
