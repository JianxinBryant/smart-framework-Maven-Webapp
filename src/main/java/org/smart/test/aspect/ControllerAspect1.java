package org.smart.test.aspect;

import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smart.framework.annotation.Aspect;
import org.smart.framework.annotation.Controller;
import org.smart.framework.proxy.AspectProxy;

@Aspect(Controller.class)
public class ControllerAspect1 extends AspectProxy{

	private static final Logger LOGGER = LoggerFactory.getLogger(ControllerAspect.class);

	private long begin;
	
	@Override
	public void before(Class<?> cls, Method method, Object[] params)
			throws Throwable {
		System.out.println("before11...");
		LOGGER.debug("----------begin---------");
		LOGGER.debug(String.format("class: %s", cls.getName()));
		LOGGER.debug(String.format("method: %s", method.getName()));
		begin = System.currentTimeMillis();
		
	}

	@Override
	public void after(Class<?> cls, Method method, Object[] params)
			throws Throwable {
		System.out.println("after22...");
		LOGGER.debug(String.format("time: %dms", System.currentTimeMillis()-begin));
		LOGGER.debug("----------end----------");
	}
	
	
}
